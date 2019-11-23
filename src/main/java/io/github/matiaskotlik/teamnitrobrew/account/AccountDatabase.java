package io.github.matiaskotlik.teamnitrobrew.account;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.matiaskotlik.teamnitrobrew.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountDatabase extends Database<Account> {
	private File storageFile;
	private List<Account> accounts;
	private ObjectMapper mapper;

	public AccountDatabase(File storageDir) {
		this.storageFile = storageDir;
		mapper = new ObjectMapper();
	}

	public AccountDatabase() {
		this(new File(System.getProperty("user.dir"), "src/main/data/accounts.json"));
	}

	@Override
	public void load() {
		try {
			accounts = mapper.readValue(storageFile, new TypeReference<List<Account>>() {});
		} catch (FileNotFoundException e) {
			accounts = new ArrayList<>();
		} catch (IOException e) {
			System.err.println("Error reading from file at " + storageFile.getPath());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void save() {
		storageFile.getParentFile().mkdirs();
		try {
			mapper.writeValue(storageFile, accounts);
		} catch (IOException e) {
			System.err.println("Error writing to file at " + storageFile.getPath());
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Account> getAll() {
		return accounts;
	}

	public List<Account> getList() {
		return accounts;
	}
}
