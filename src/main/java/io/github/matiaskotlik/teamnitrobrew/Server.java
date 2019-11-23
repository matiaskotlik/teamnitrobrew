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

		accountDatabase.getList().add(new Account(UUID.randomUUID().toString(),
				"rachel",
				new PasswordHasher().getHashedPassword("password"),
				"Rachel Yao",
				"I am a student at Naperville North High School. I enjoy art and computer science. I created Tutor Time with my teammates in order to provide a platform for high school students to get easy and instant access to tutoring without having to schedule ahead of the time.",
				"rachel.jpeg",
				Arrays.asList(4, 5, 5),
				Arrays.asList(
						new Blog("Mrs. Moore's 1st Period Class",
								"AP Calculus BC",
								"This course includes all topics covered in AP Calculus AB. Additional topics include sequences and series, polar coordinates, vector functions, additional integration methods, and differential equations. Students desiring two semesters of college placement/credit will be encouraged to take the Advanced Placement Exam. A graphing calculator is required.",
								"calc.jpg"),
						new Blog("Mr. Rowzee's 3rd Period Class",
								"AP Physics 1",
								"Physics 1 is an inquiry-based course designed to expand on the principles of how and why the world around us works and find practical applications of physics through labs, data analysis, problem solving, and discussions. Students will investigate the topics of motion, force, energy, momentum, waves, rotational motion, electrostatics, and electricity. Students taking this course should be self-directed learners with strong mathcomputational skills..",
								"physics.jpg"),
						new Blog("Ms. Parato's 8th Period Class",
								"AP Language and Composition",
								"Per the College Board description of the English Language and Composition course (2006), AP® Language and Composition is a college-level course designed to prepare students to “become skilled writers who compose for a variety of purposes, aware of the interactions among a writer’s purposes, audience expectations, and subjects. An integral part of [this] course [is] the development of research skills that enable [you] to evaluate, use, and cite source material.",
								"lang.png")
				),
				13.64
		));
		accountDatabase.save();

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

		get("/tutor", (req, res) -> {
			return render(getBase(req), "tutor.ftlh");
		});

		get("/profile/:user", (req, res) -> {
			String user = req.params("user");
			Account acc = accountDatabase.getAll().stream().filter(a -> a.getUsername().equals(user)).findAny().orElse(null);
			Map<String, Object> data = getBase(req);
			if (acc != null) {
				data.put("user", acc);
			}
			return render(data, "profile.ftlh");
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

		post("/search", (req, res) -> {
			String am = req.queryParams("type");
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
					return "";
			}
			String room = getRoom(subject, am, looking);
			res.redirect("/videocall?room=" + room + "&role=" + am);
			return "";
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
