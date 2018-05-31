package app.utils;

import app.model.Block;
import app.model.Contract;
import app.model.Transaction;
import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Keyz.GenerateHash;
import static app.model.Keyz.GenerateSeed;
import static app.model.dto.FullUserData.FULL;
import static app.model.dto.FullUserData.REDEEM;
import static app.model.dto.FullUserData.VIEW;
import static app.service.BlockManager.CreateAndVerifyContractsAndTransactions;
import static app.service.BlockManager.CreateBlockAndVerify;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.CreateKey;
import static app.service.RegistrationManager.CreateRegistration;
import static app.service.UserManager.CreateUser;
import static app.service.UserManager.Users;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class FileUtil {
    public static final String MERKLE_FILENAME = "MERKLE.dat";
    public static final String KEYS_FILENAME = "KEYS.dat";
    public static final String USERS_FILENAME = "USERS.dat";
    public static final String REGISTERED_FILENAME = "RPU.dat";
    public static final String CODES_FILENAME = "ACTIVATION.dat";
    public static final String BLOCKS_FILENAME = "BLOCKS.dat";
    public static final String CONTRACTS_FILENAME = "CONTRACTS.dat";
    public static final String TRANSACTIONS_FILENAME = "TRANSACTIONS.dat";

    public static void InitServerLoadData() {
        System.out.println("Initialization started");
        ReadAllKeys();
        ReadAllUsers();
        ReadAllRegistrations();
        ReadAllBlocks();
        //Read all blocks must occur before read all transactions as it verifies
        ReadAllContractsAndTransactions();
        System.out.println("Contracts and Transactions Verified");
        CreateAndStoreKey("Miner");

        System.out.println("Generated random keys");

        Users.putIfAbsent("tw@gmail.com", new FullUserData("tw@gmail.com",
                "b585d58817fe1d6ee882bd20ef3580b5bdfcb5cba4e315a11e89d6652159ad9c18ee0bbd9ce0684bf2b99254ceec82e4b4dfb7ea5ea2ffcbee8dbd4a5c7a68a7",
                GenerateHash("tw@gmail.com",6), DateUtil.GetDateTimeTomorrow(), FULL));

        System.out.println("Able to create users");

        Users.putIfAbsent("forfive@gmail.com", new FullUserData("forfive@gmail.com",
                "cec41ebc893f5b6bc8a0b59460ead94656974f93babafd71cce5ed58624b5f4a0e69edc3400d6d3d335a305817d70d5c1732b1fc82d374ee84e8c63aa6856938",
                GenerateHash("forfive@gmail.com",6), DateUtil.GetDateTimeTomorrow(), VIEW));

        Users.putIfAbsent("simonsips@gmail.com", new FullUserData("simonsips@gmail.com",
                "74f34c5ff2c288d5aaac3d99b2c497df2c434f1e6a9498befd95529815f43c32bd65c271045c00935cd5b97499c49c0b921397ef7c0d58f25f5bbe141aba22c6",
                GenerateHash("simon",6), DateUtil.GetDateTimeTomorrow(), VIEW));


        Users.putIfAbsent("kroger@gmail.com", new FullUserData("kroger@gmail.com",
                "921a446c39ee2339c41e7e1a76240f4bc48766881a94a33271fc43152a2fb34203d892ed6a357ccda4cff4be9f86280c49018bdd2a4b3fe517141e4b9b2e2b02",
                GenerateHash("kroger",6), DateUtil.GetDateTimeTomorrow(), REDEEM));


        Users.putIfAbsent("walmart@gmail.com", new FullUserData("walmart@gmail.com",
                "ac415603b07b59c20f14c04bdf7bc839fccdf424c565a11a5dac6fadc19010d15f79b586d08757a52d4770964b2275779db18ba6f3625ec61ea4fe3c0f571d40",
                GenerateHash("walmart",6), DateUtil.GetDateTimeTomorrow(), VIEW));


        Users.putIfAbsent("target@gmail.com", new FullUserData("target@gmail.com",
                "c9d23dec55be9e9f7722a78131fb30e9a7b90ef0f22c9274a7c480231014921b6ad7312f3dbc1504d4227ee796e98462f0c999536bb558148a7de76ca32375e1",
                GenerateHash("target",6), DateUtil.GetDateTimeTomorrow(), VIEW));


        Users.putIfAbsent("walgreens@gmail.com", new FullUserData("walgreens@gmail.com",
                "edb5f9116e5cfdf58ab783fe9f47686d333fd8ca6549fd22f49564d7efa44293953ce044523d97fad570b6f9f65627cff859a5a64e38528daab0c3ac24f2f8f7",
                GenerateHash("walgreens",6), DateUtil.GetDateTimeTomorrow(), VIEW));


        Users.putIfAbsent("albertsons@gmail.com", new FullUserData("albertsons@gmail.com",
                "1a9fe37fedbcba0637ee2ca868f34221ff33d3da0dfdddd59a6490c2074e2a3880ecac592f0df3c165c706fa4da658f71963c659caccb58fba19648d28cb813e",
                GenerateHash("albertsons",6), DateUtil.GetDateTimeTomorrow(), VIEW));


        Users.putIfAbsent("cvs@gmail.com", new FullUserData("cvs@gmail.com",
                "9809037b3ec42d44ca4fa81ab033a97afbf3850e7824e246e715276658fef352060cc3a993e611d2f815831589a3e194d42bc43517d2c8be09a4dd4f57b2040a",
                GenerateHash("cvs",6), DateUtil.GetDateTimeTomorrow(), VIEW));

        System.out.println("Init Success");
    }

    public static void AppendLine(String block, String fileName) {
        WriteLine(block, fileName, true, true);
    }

    public static void WriteLine(String block, String fileName, boolean newline, boolean append) {
        try {
            Writer output = new BufferedWriter(new FileWriter(fileName, append));
            output.append(block + (newline ? "\n" : ""));
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void StoreKey(String block) {
        AppendLine(block, KEYS_FILENAME);
    }

    public static void ReadAllKeys() {
        try {
            Scanner scanner = new Scanner(new File(KEYS_FILENAME));
            String line = scanner.nextLine();

            CreateKey(line);
            while (true) {
                line = scanner.nextLine();
                CreateKey(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }


    public static void StoreUser(FullUserData user) {
        AppendLine(ToJSON(user), USERS_FILENAME);
    }

    public static void ReadAllUsers() {
        try {
            Scanner scanner = new Scanner(new File(USERS_FILENAME));
            String line = scanner.nextLine();

            CreateUser(line);
            while (true) {
                line = scanner.nextLine();
                CreateUser(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static Activation StoreRegistration(LoginDetails loginDetails, String activationCode) {
        AppendLine(ToJSON(loginDetails), REGISTERED_FILENAME);
        Activation activation = new Activation(loginDetails.email, activationCode);
        AppendLine(ToJSON(activation), CODES_FILENAME);
        return activation;
    }

    public static void ReadAllRegistrations() {
        ConcurrentMap<String, String> registrations = new ConcurrentHashMap<>();
        ConcurrentMap<String, String> codes = new ConcurrentHashMap<>();
        try {
            Scanner scanner = new Scanner(new File(REGISTERED_FILENAME));
            Scanner scanner2 = new Scanner(new File(CODES_FILENAME));
            String line = scanner.nextLine();
            String line2 = scanner2.nextLine();
            registrations.putIfAbsent(FromJSON(line, LoginDetails.class).email, line);
            codes.putIfAbsent(FromJSON(line2, Activation.class).email, line2);

            while (true) {
                line = scanner.nextLine();
                line2 = scanner2.nextLine();
                registrations.putIfAbsent(FromJSON(line, LoginDetails.class).email, line);
                codes.putIfAbsent(FromJSON(line2, Activation.class).email, line2);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
        for (String email : registrations.keySet()) {
            try {
                CreateRegistration(registrations.get(email), codes.get(email));
            } catch (IllegalArgumentException e) {
                registrations.remove(email);
                codes.remove(email);
            }
        }

        String[] registrationValues = registrations.values().toArray(new String[0]);

        for (int i = 0; i < registrationValues.length; i++) {
            if (i == 0) {
                WriteLine(registrationValues[i], REGISTERED_FILENAME, true, false);
            } else {
                AppendLine(registrationValues[i], REGISTERED_FILENAME);

            }
        }

        String[] codesValues = codes.values().toArray(new String[0]);

        for (int i = 0; i < codesValues.length; i++) {
            if (i == 0) {
                WriteLine(codesValues[i], CODES_FILENAME, true, false);
            } else {
                AppendLine(codesValues[i], CODES_FILENAME);

            }
        }

    }

    public static void ReadAllBlocks() {
        try {
            Scanner scanner = new Scanner(new File(BLOCKS_FILENAME));
            String line = scanner.nextLine();
            CreateBlockAndVerify(line);

            while (true) {
                line = scanner.nextLine();
                CreateBlockAndVerify(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static void ReadAllContractsAndTransactions() {
        try {
            Scanner scanner = new Scanner(new File(CONTRACTS_FILENAME));
            Scanner scanner0 = new Scanner(new File(TRANSACTIONS_FILENAME));
            String line = scanner.nextLine();
            String line0 = scanner0.nextLine();
            CreateAndVerifyContractsAndTransactions(line, line0);

            while (true) {
                line = scanner.nextLine();
                line0 = scanner0.nextLine();
                CreateAndVerifyContractsAndTransactions(line, line0);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static void StoreBlockchainBlock(Block block) {
        AppendLine(ToJSON(block), BLOCKS_FILENAME);
    }

    public static void StoreContractsInBlock(List<Contract> contracts) {
        AppendLine(ToJSON(contracts.toArray()), CONTRACTS_FILENAME);
    }

    public static void StoreTransactionsInBlock(List<Transaction> transactions) {
        AppendLine(ToJSON(transactions.toArray()), TRANSACTIONS_FILENAME);
    }

    public static void StoreMerkleDataLog(String merkleData) {
        AppendLine(merkleData, MERKLE_FILENAME);
    }

}
