package dev.thezexquex.buggybedwars.event;

import dev.thezexquex.buggybedwars.logic.team.Team;

public class TeamBedRebuildEvent extends TeamEvent {
    public TeamBedRebuildEvent(Team team) {
        super(team);
    }
}
