package io.github.matiaskotlik.teamnitrobrew;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	private FreeMarkerEngine engine;
	private AccountDatabase accountDatabase;
	private PasswordHasher passwordHasher;

	public Server(int port, boolean debug) {
		this.port = port;
		this.debug = debug;
	}

	public void start() {
		port(port);

		String projectDir = System.getProperty("user.dir");
		if (debug) {
			String staticDir = "src/main/resources/public";
			staticFiles.externalLocation(projectDir + File.separator + staticDir);
		} else {
			staticFiles.location("/public");
		}

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
		if (debug) {
			makeDebugPaths();
		}
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
