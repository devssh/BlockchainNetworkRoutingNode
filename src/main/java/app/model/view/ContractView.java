package app.model.view;

import app.model.Contract;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class ContractView {
    public final ConcurrentMap<String, Contract> contracts;
}
