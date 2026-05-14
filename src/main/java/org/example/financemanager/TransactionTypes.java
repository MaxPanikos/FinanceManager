package org.example.financemanager;

public enum TransactionTypes {
    OTHER_INCOME("Příjem", "Ostatní / Nezařazeno"),
    OTHER_EXPENSE("Výdaj", "Ostatní / Nezařazeno"),
    SALARY("Příjem", "Mzda"),
    GIFT("Příjem", "Dar"),
    FOOD("Výdaj", "Potraviny"),
    RENT("Výdaj", "Bydlení"),
    TRANSPORT("Výdaj", "Doprava"),
    ENTERTAINMENT("Výdaj", "Zábava");

    private final String type;
    private final String label;

    TransactionTypes(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public String getType() { return type; }
    public String getLabel() { return label; }
}
