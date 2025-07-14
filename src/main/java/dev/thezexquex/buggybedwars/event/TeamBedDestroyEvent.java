package dev.thezexquex.buggybedwars.event;

import dev.thezexquex.buggybedwars.logic.team.Team;

public class TeamBedDestroyEvent extends TeamEvent {
    public TeamBedDestroyEvent(Team team) {
        super(team);
    }
}
