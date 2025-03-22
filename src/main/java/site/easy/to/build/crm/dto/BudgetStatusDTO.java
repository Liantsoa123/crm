package site.easy.to.build.crm.dto;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BudgetStatusDTO {
    private Budget budget;
    private BigDecimal remainingAmount;
    private String alertMessage;
    private boolean isAlert;

    public BudgetStatusDTO(Budget budget, List<Expense> expenses, Double alertRate) {
        this.budget = budget;

        // Calculer le montant restant
        BigDecimal totalExpenses = expenses.stream()
                .filter(expense -> expense.getBudget().getId().equals(budget.getId()))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.remainingAmount = budget.getAmount().subtract(totalExpenses);

        // Vérifier si le montant restant est inférieur au seuil d'alerte
        BigDecimal threshold = budget.getAmount().multiply(BigDecimal.valueOf(alertRate));
        this.isAlert = this.remainingAmount.compareTo(threshold) < 0;

        // Créer un message d'alerte approprié
        if (isAlert) {
            this.alertMessage = "ALERTE: Le budget " + budget.getName() +
                    " a atteint un niveau critique. Reste: " +
                    remainingAmount + " € sur " + budget.getAmount() + " €";
        } else {
            this.alertMessage = "Budget " + budget.getName() +
                    " : Reste " + remainingAmount + " € sur " +
                    budget.getAmount() + " €";
        }
    }

    // Getters et Setters
    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    // Méthodes utilitaires
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(budget.getStartDate()) || now.isAfter(budget.getStartDate())) &&
                (now.isEqual(budget.getEndDate()) || now.isBefore(budget.getEndDate()));
    }

    public double getUsagePercentage() {
        if (budget.getAmount().equals(BigDecimal.ZERO)) {
            return 0.0;
        }
        return 100.0 - (remainingAmount.doubleValue() * 100.0 / budget.getAmount().doubleValue());
    }
}