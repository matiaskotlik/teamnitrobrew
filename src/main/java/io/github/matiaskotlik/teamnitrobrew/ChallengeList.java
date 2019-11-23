package io.github.matiaskotlik.teamnitrobrew;

import java.util.ArrayList;
import java.util.Collection;

public class ChallengeList extends ArrayList<Challenge> {
	public ChallengeList() {
		super();
	}

	public ChallengeList(Collection<? extends Challenge> c) {
		super(c);
	}
}
