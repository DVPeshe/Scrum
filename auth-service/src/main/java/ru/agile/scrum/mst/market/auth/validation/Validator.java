package ru.agile.scrum.mst.market.auth.validation;

import ru.agile.scrum.mst.market.api.ValidationError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Validator<T> {

    Map<Predicate<T>, ValidationError> rules = new HashMap<>();

    public void addRule(Predicate<T> rule, ValidationError error) {
        rules.put(rule, error);
    }

    public List<ValidationError> validate(T obj) {
        return rules.keySet().stream()
                .filter(rule -> rule.test(obj))
                .map(rule -> rules.get(rule))
                .toList();
    }
}
