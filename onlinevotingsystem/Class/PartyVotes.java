package com.onlie.voting.onlinevotingsystem.Class;

public class PartyVotes {
    String partyName,partyVotes;

    public PartyVotes() {
    }

    public PartyVotes(String partyName, String partyVotes) {
        this.partyName = partyName;
        this.partyVotes = partyVotes;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyVotes() {
        return partyVotes;
    }

    public void setPartyVotes(String partyVotes) {
        this.partyVotes = partyVotes;
    }
}
