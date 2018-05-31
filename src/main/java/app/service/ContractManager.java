package app.service;

import app.model.Contract;
import app.model.dto.CreateContract;
import app.model.utxo.ContractUTXO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ContractManager {
    public static final ConcurrentMap<String, Contract> Contracts = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, ContractUTXO> ContractUTXOs = new ConcurrentHashMap<>();

    public static void CreateContractUTXO(CreateContract createContract) {
        //TODO: check Contracts to see if it is already existing before creating the UXTO
        ContractUTXO contractUTXO = new ContractUTXO(createContract);
        ContractUTXOs.putIfAbsent(contractUTXO.name, contractUTXO);
    }

    public static void CreateContracts(List<Contract> contracts) {
        contracts.forEach(contract -> Contracts.putIfAbsent(contract.name, contract));
    }

}
