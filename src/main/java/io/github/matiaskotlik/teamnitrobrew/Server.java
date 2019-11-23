package io.github.matiaskotlik.teamnitrobrew;

import com.pusher.rest.Pusher;
import com.pusher.rest.data.PresenceUser;
import freemarker.template.Configuration;
import freemarker.template.Version;
import io.github.matiaskotlik.teamnitrobrew.account.Account;
import io.github.matiaskotlik.teamnitrobrew.account.AccountDatabase;
import io.github.matiaskotlik.teamnitrobrew.crypt.PasswordHasher;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class Server {
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
		if (debug) {
			makeDebugPaths();
		}
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

	private void makeDebugPaths() {
		path("/debug", () -> {
			get("/save", (req, res) -> {
				accountDatabase.save();

				return "Saved!";
			});
			get("/load", (req, res) -> {
				accountDatabase.load();

				return "Loaded!";
			});
			get("/add", (req, res) -> {
				accountDatabase.getList().add(new Account("matias",
						passwordHasher.getHashedPassword("password")));
				return "added matias:password to list";
			});
			get("/session", (req, res) -> {
				StringBuilder listString = new StringBuilder();
				for (String key : req.session().attributes()) {
					listString.append(key).append(": ").append(req.session().attribute(key).toString()).append("<br />");
				}
				return listString.toString();
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

	private void makePaths() {
		get("/", (req, res) -> {
			return render(getBase(req), "index.ftlh");
		});

		notFound((req, res) -> {
			return render(getBase(req), "404.ftlh");
		});

		internalServerError((req, res) -> {
			return render(getBase(req), "500.ftlh");
		});
	}

	public String render(Map<String, Object> attributes, String templatePath) {
		return engine.render(new ModelAndView(attributes, templatePath));
	}
}
