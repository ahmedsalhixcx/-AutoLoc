package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Client;

import java.util.List;

public interface IClientService {

    Client addClient(Client client);

    Client updateClient(Client client);

    Client retrieveClient(Long id);

    List<Client> retrieveAllClients();

    void removeClient(Long id);
}
