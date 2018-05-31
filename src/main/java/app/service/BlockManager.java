package app.service;

import app.model.Block;
import app.model.BlockDetails;
import app.model.Contract;
import app.model.Transaction;
import org.springframework.core.io.InputStreamSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Block.BlockSign;
import static app.model.Keyz.GenerateHash;
import static app.model.Keyz.GenerateSeed;
import static app.service.ContractManager.ContractUTXOs;
import static app.service.ContractManager.Contracts;
import static app.service.ContractManager.CreateContracts;
import static app.service.MailService.SendMail;
import static app.service.PasskitService.CreatePass;
import static app.service.TransactionManager.CreateTransactions;
import static app.service.TransactionManager.TransactionUTXOs;
import static app.service.TransactionManager.Transactions;
import static app.utils.DateUtil.GetDateTimeNow;
import static app.utils.FileUtil.*;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static app.utils.SignatureUtils.Verify;

public class BlockManager {
    public static final ConcurrentMap<Integer, Block> BLOCKCHAIN = new ConcurrentHashMap<>();
    public static final ConcurrentMap<Integer, Contract[]> BLOCKCHAIN_CONTRACTS = new ConcurrentHashMap<>();
    public static final ConcurrentMap<Integer, Transaction[]> BLOCKCHAIN_TRANSACTIONS = new ConcurrentHashMap<>();

    public static void MineBlock() {
        //Extract to mine node
        if (ContractUTXOs.keySet().size() == 0 && TransactionUTXOs.keySet().size() == 0) {
            System.out.println("Mining active");
            return;
        }

        int blockDepth = BLOCKCHAIN.keySet().size();

        List<String> merkleDataList = new ArrayList<>();


        Set<String> contractKeySet = ContractUTXOs.keySet();
        String[] contractKeys = contractKeySet.toArray(new String[contractKeySet.size()]);

        List<Contract> contracts = new ArrayList<>();
        for (int i = 0; i < contractKeySet.size(); i++) {
            Contract contract = new Contract(ContractUTXOs.get(contractKeys[i]), blockDepth, i);
            contracts.add(contract);
            //TODO: Test to see if stream map collect gives same data in sequence
            merkleDataList.add(ToJSON(contract));
        }


        Set<String> transactionKeySet = TransactionUTXOs.keySet();
        String[] transactionKeys = transactionKeySet.toArray(new String[transactionKeySet.size()]);

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transactionKeySet.size(); i++) {
            Transaction transaction = new Transaction(TransactionUTXOs.get(transactionKeys[i]), blockDepth, contracts.size() + i);
            transactions.add(transaction);
            //TODO: Test to see if stream map collect gives same data in sequence
            merkleDataList.add(ToJSON(transaction));
        }


        String merkleData = ToJSON(merkleDataList.toArray());
        StoreMerkleDataLog(merkleData);


//      same miner as the one who mines the block inside block constructor
        String merkleRoot = BlockSign(merkleData);
        String genesisStringToProveMinimumDateTime = new StringBuffer("Genesis block timestamp: " +
                "Mark Zuckerberg answers to congress over Cambridge Analytica").reverse().toString();
        String previousSign = blockDepth == 0 ? genesisStringToProveMinimumDateTime : BLOCKCHAIN.get(blockDepth - 1).sign;

        BlockDetails blockDetails = new BlockDetails(0, blockDepth, GetDateTimeNow(),
                previousSign, merkleRoot, 3, "me", contractKeys.length + transactionKeys.length);

        //Mining logic here
        int difficulty = 3;
        String difficultyString = "";
        String difficultyCharacter = "0";
        for (int i = 0; i < difficulty - 1; i++) {
            difficultyString = difficultyString + difficultyCharacter;
        }
        difficultyString = difficultyString + "=";
        int incrementNonceBy = 1;


        String data = ToJSON(blockDetails);
        Block block = new Block(data);
        while (!block.sign.substring(block.sign.length() - difficulty, block.sign.length()).equals(difficultyString)) {
            blockDetails.nonce = blockDetails.nonce + incrementNonceBy;
            blockDetails.blockCreatedAt = GetDateTimeNow();
            data = ToJSON(blockDetails);
            block = new Block(data);
        }

        // cleanup steps here
        contractKeySet.forEach(ContractUTXOs::remove);
        transactionKeySet.forEach(TransactionUTXOs::remove);
        BLOCKCHAIN.putIfAbsent(blockDepth, block);
        BLOCKCHAIN_CONTRACTS.putIfAbsent(blockDepth, contracts.toArray(new Contract[contracts.size()]));
        BLOCKCHAIN_TRANSACTIONS.putIfAbsent(blockDepth, transactions.toArray(new Transaction[transactions.size()]));
        //if the above 3 lines are successful do things like sending email here if in contract

        CreateContracts(contracts);
        CreateTransactions(transactions);
        StoreBlockchainBlock(block);
        StoreContractsInBlock(contracts);
        StoreTransactionsInBlock(transactions);

        System.out.println("Block mined");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (transaction.contractName.startsWith("Create")) {
                String[] values = transaction.values;
                if (values.length > 2) {
                    for (int j = 2; j < values.length; j++) {
                        InputStreamSource pkpass = CreatePass(transaction.contractName.split("Create-")[1] + "," + values[0] +
                                        "," + values[1] + "," + values[j] + "," + GenerateHash(values[j], 6),
                                "Offer", values[0], values[1]);
                        SendMail(values[j], "Discount Coupon " + transaction.contractName.split("Create-")[1],
                                "Scan the code below to claim a discount of " + values[1] + "% on " + values[0] + ".", pkpass);
                        System.out.println("Mail sent to " + values[j]);
                    }
                }
            }
        }


    }

    public static void CreateBlockAndVerify(String jsonBlock) {
        Block block = FromJSON(jsonBlock, Block.class);
        BlockDetails blockDetails = block.blockData;
        block.data = ToJSON(block.blockData);
        BLOCKCHAIN.putIfAbsent(blockDetails.depth, block);
        if (!Verify(block)) {
            throw new IllegalArgumentException("Blocks don't match up, blockchain in inconsistent state, re-sync from external source");
        }
    }

    public static void CreateAndVerifyContractsAndTransactions(String jsonContracts, String jsonTransactions) {
        List<String> merkleDataList = new ArrayList<>();

        Contract[] contracts = FromJSON(jsonContracts, Contract[].class);
        Transaction[] transactions = FromJSON(jsonTransactions, Transaction[].class);
        if (contracts == null) {
            contracts = new Contract[0];
        }
        if (transactions == null) {
            transactions = new Transaction[0];
        }

        Integer blockDepth = contracts.length > 0 ? contracts[0].address.blockDepth : transactions[0].address.blockDepth;

        BLOCKCHAIN_CONTRACTS.putIfAbsent(blockDepth, contracts);
        for (int i = 0; i < contracts.length; i++) {
            Contract contract = contracts[i];
            Contracts.putIfAbsent(contract.name, contract);
            if (contract.address.transactionDepth != i) {
                throw new IllegalArgumentException("Contracts don't match up, blockchain in inconsistent state, re-sync from external source");
            }

            merkleDataList.add(ToJSON(contract));
        }


        BLOCKCHAIN_TRANSACTIONS.putIfAbsent(blockDepth, transactions);
        for (int i = 0; i < transactions.length; i++) {
            Transaction transaction = transactions[i];
            Transactions.putIfAbsent(transaction.contractName, transaction);
            if (transaction.address.transactionDepth != contracts.length + i) {
                throw new IllegalArgumentException("Transactions don't match up, blockchain in inconsistent state, re-sync from external source");
            }

            merkleDataList.add(ToJSON(transaction));
        }

        //Make sure blockchain is populated with CreateBlockAndVerify before running this part
        Block block = BLOCKCHAIN.get(blockDepth);
        BlockDetails blockDetails = FromJSON(block.data, BlockDetails.class);


        String merkleData = ToJSON(merkleDataList.toArray());

        if (!Verify(merkleData, block.publicKey, blockDetails.merkleRoot)) {
            throw new IllegalArgumentException("Merkle roots don't match up, blockchain in inconsistent state, re-sync from external source");
        }
        System.out.println("Block Verified");
    }

}
