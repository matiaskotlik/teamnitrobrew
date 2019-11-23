package io.github.matiaskotlik.teamnitrobrew;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pusher.rest.Pusher;
import com.pusher.rest.data.PresenceUser;
import com.pusher.rest.data.Result;
import freemarker.template.Configuration;
import freemarker.template.Version;
import io.github.matiaskotlik.teamnitrobrew.account.Account;
import io.github.matiaskotlik.teamnitrobrew.account.AccountDatabase;
import io.github.matiaskotlik.teamnitrobrew.crypt.HashedPassword;
import io.github.matiaskotlik.teamnitrobrew.crypt.PasswordHasher;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class Server {
	public static ObjectMapper mapper = new ObjectMapper();
	public static void main(String[] args) {
		int port = 8080; // default port
		boolean debug = true; // debug mode

		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Port not specified, binding to " + port);
		} catch (NumberFormatException e) {
			System.out.println("Invalid port, defaulting to " + port);
		}

		new Server(port, debug).start();
	}

	private int port;
	private boolean debug;
	private Pusher pusher;
	private FreeMarkerEngine engine;
	private AccountDatabase accountDatabase;
	private PasswordHasher passwordHasher;

	public Server(int port, boolean debug) {
		this.port = port;
		this.debug = debug;
	}

	public void start() {
		port(port);

		// static files
		String projectDir = System.getProperty("user.dir");
		if (debug) {
			String staticDir = "src/main/resources/public";
			staticFiles.externalLocation(projectDir + File.separator + staticDir);
		} else {
			staticFiles.location("/public");
		}

		// connect to pusher
		String secret = null;
		try {
			secret = new String(Files.readAllBytes(Paths.get(projectDir, "secret.txt"))).trim();
		} catch (IOException e) {
			System.err.println("No secret file found, please add `secret.txt` to the root directory with the secret in it");
			return;
		}

		pusher = new Pusher("905167", "d964cc6cb9f216c957f8", secret);
		pusher.setCluster("us2");
		pusher.setEncrypted(true);

		Configuration configuration = new Configuration(new Version(2, 3, 23));
		boolean fail = false;
		if (debug) {
			String templateDir = "src/main/resources/template";
			try {
				configuration.setDirectoryForTemplateLoading(new File(projectDir, templateDir));
			} catch (IOException e) {
				fail = true;
			}
		}
		if (fail || !debug) {
			// Server's resource folder, NEED SLASH IN FRONT
			configuration.setClassForTemplateLoading(Server.class, "/template");
		}

		engine = new FreeMarkerEngine(configuration);
		passwordHasher = new PasswordHasher();
		accountDatabase = new AccountDatabase();
		accountDatabase.load();

		makePaths();
		makePusherPaths();
	}

	private void makePusherPaths() {
		path("/pusher", () -> {
			post("/auth", (req, res) -> {
				String socket_id = req.queryParams("socket_id");
				String channel_name = req.queryParams("channel_name");
				String id = UUID.randomUUID().toString() + new Date().toString();
				PresenceUser presenceUser = new PresenceUser(id.hashCode());
				return pusher.authenticate(socket_id, channel_name, presenceUser);
			});
		});
	}

	private Map<String, Object> getBase(Request req) {
		Map<String, Object> attributes = new HashMap<>();
		Account account = req.session().attribute("account");
		if (account != null) {
			attributes.put("account", account);
		}
		return attributes;
	}

	private String getRoom(String subject, String am, String looking) {
		Result result = pusher.get("/channels",
				new HashMap<String, String>() {{
						put("filter_by_prefix", "presence-" + subject + "-" + looking);
						put("info", "user_count");
					}});
		if (result.getStatus() == Result.Status.SUCCESS) {
			String channelListJson = result.getMessage();
			PusherResponse ps;
			try {
				ps = mapper.readValue(channelListJson, PusherResponse.class);
			} catch (IOException e) {
				System.out.println("fuckkkk");
				e.printStackTrace();
				System.out.println(channelListJson);
				return "presence-error";
			}
			for (Map.Entry<String, Channel> e : ps.getChannels().entrySet()) {
				if (e.getValue().getUserCount() == 1) {
					return e.getKey();
				}
			}
			return "presence-" + subject + "-" + am + UUID.randomUUID().toString();
		}
		return "presence-error";
	}

	private void makePaths() {
		get("/", (req, res) -> {
			return render(getBase(req), "index.ftlh");
		});

		post("/login", (req, res) -> {
			Map<String, Object> data = getBase(req);
			String user = req.queryParams("uname");
			if (user != null) {
				String pass = req.queryParams("psw");
				Account account = accountDatabase.getAll().stream().filter(a -> a.getUsername().equals(user))
						.findAny().orElse(null);
				if (account != null && pass != null) {
					HashedPassword hp = account.getHashedPassword();
					if (new PasswordHasher().check(pass, hp)) {
						req.session().attribute("account", account);
					}
				}
			}
			return render(data, "index.ftlh");
		});

		get("/tutor", (req, res) -> {
			Map<String, Object> map = getBase(req);
			map.put("type", req.queryParamOrDefault("type", "tutee"));
			return render(map, "tutor.ftlh");
		});

		get("/profile/:user", (req, res) -> {
			String user = req.params("user");
			Account acc = accountDatabase.getAll().stream().filter(a -> a.getUsername().equals(user)).findAny().orElse(null);
			Map<String, Object> data = getBase(req);
			if (acc != null) {
				data.put("user", acc);
				return render(data, "profile.ftlh");
			}
			res.status(404);
			return render(data, "404.ftlh");
		});

		post("/rate", (req, res) -> {
			String id = req.queryParams("id");
			int rate = Integer.parseInt(req.queryParams("rate"));
			accountDatabase.getAll().stream().filter(a -> a.getId().equals(id)).findAny()
					.ifPresent(a -> {
						if (rate > 2) {
							a.setFunds(a.getFunds() + 2);
						}
						a.updateAvgRating(rate);
					});
			accountDatabase.save();
			res.redirect("/");
			return "";
		});

		post("/videocall", (req, res) -> {
			String am = req.queryParams("type");
			System.out.println(am);
			String looking;
			String subject = req.queryParams("subject");
			String id = "";
			switch (am) {
				case "tutor":
					looking = "tutee";
					break;
				case "tutee":
					looking = "tutor";
					Account account = req.session().attribute("account");
					if (account != null) {
						id = "&id=" + account.getId();
					}
					break;
				default:
					res.status(404);
					return render(getBase(req), "404.ftlh");
			}
			String room = getRoom(subject, am, looking);
			res.redirect("/videocall?room=" + room + "&role=" + am);
			System.out.println(am + ", " + looking);
			return "asdf";
		});

		get("/videocall", (req, res) -> {
			return render(getBase(req), "videocall.ftlh");
		});

		notFound((req, res) -> {
			return render(getBase(req), "404.ftlh");
		});

		internalServerError((req, res) -> {
			return render(getBase(req), "500.ftlh");
		});
	}

	public String genRoom() {
		return UUID.randomUUID().toString();
	}

	public String render(Map<String, Object> attributes, String templatePath) {
		return engine.render(new ModelAndView(attributes, templatePath));
	}
}
