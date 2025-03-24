package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @Column(name = "expense_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = true)
    private Budget budget;

    @Column(name = "description")
    private String description;


    // Constructors
    public Expense() {
    }

    // Getters and Setters
    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long id) {
        this.expenseId = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}