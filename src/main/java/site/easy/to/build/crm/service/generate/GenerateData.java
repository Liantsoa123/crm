package site.easy.to.build.crm.service.generate;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.user.UserServiceImpl;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.math.BigDecimal;
import java.sql.SQLDataException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class GenerateData {

    private final Faker faker = new Faker();
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private final EntityManager entityManager;


    /**
     * Génère des clients aléatoires avec Faker
     *
     * @param count Nombre de clients à générer
     * @return Liste des CustomerLoginInfo générés
     */
    public List<CustomerLoginInfo> generateRandomCustomers(int count) {
        User admin = userServiceImpl.findFirstByOrderByIdAsc();
        List<CustomerLoginInfo> customersInfos = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String username = faker.name().username();
            String email = username.toLowerCase().replace(" ", "") + "@gmail.com";

            Customer customer = new Customer();
            customer.setName(username);
            customer.setPosition(faker.company().profession());
            customer.setEmail(email);
            customer.setUser(admin);
            customer.setCountry(faker.address().country());
            customer.setCity(faker.address().city());

            CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
            customerLoginInfo.setEmail(email);
            customerLoginInfo.setPasswordSet(true);
            String hashPassword = passwordEncoder.encode("2004");
            customerLoginInfo.setPassword(hashPassword);
            String token = EmailTokenUtils.generateToken();
            customerLoginInfo.setToken(token);
            customerLoginInfo.setCustomer(customer);

            customersInfos.add(customerLoginInfo);
        }
        return customersInfos;
    }

    /**
     * Génère des budgets aléatoires pour les clients existants
     *
     * @param customers Liste des clients
     * @param count     Nombre de budgets à générer par client
     * @return Liste des budgets générés
     */
    public List<Budget> generateRandomBudgets(List<Customer> customers, int count) {
        List<Budget> budgets = new ArrayList<>();

        for (Customer customer : customers) {
            for (int i = 0; i < count; i++) {
                Budget budget = new Budget();

                budget.setName(faker.commerce().department() + " Budget");
                // Montant aléatoire entre 1000 et 50000
                double amount = 1000 + (Math.random() * 49000);
                budget.setAmount(BigDecimal.valueOf((double) Math.round(amount * 100) / 100));

                LocalDate now = LocalDate.now();
                LocalDate startDate = now.minusDays(ThreadLocalRandom.current().nextInt(0, 60));
                LocalDate endDate = startDate.plusMonths(ThreadLocalRandom.current().nextInt(1, 13));

                budget.setStartDate(startDate);
                budget.setEndDate(endDate);
                budget.setCustomer(customer);

                budgets.add(budget);
            }
        }
        return budgets;
    }

    /**
     * Génère des tickets aléatoires pour les clients existants
     *
     * @param customers Liste des clients
     * @param count     Nombre de tickets à générer par client
     * @return Liste des tickets générés
     */
    public List<Ticket> generateRandomTickets(List<Customer> customers, int count) {
        User admin = userServiceImpl.findFirstByOrderByIdAsc();
        List<Ticket> tickets = new ArrayList<>();
        String[] statuses = { "open","assigned","on-hold","in-progress","resolved","closed","reopened","pending-customer-response","escalated","archived" };
        String[] priorities = { "low","medium","high","closed","urgent","critical" };

        for (Customer customer : customers) {
            for (int i = 0; i < count; i++) {
                Ticket ticket = new Ticket();

                Expense expense = new Expense();
                // Montant aléatoire entre 50 et 2000
                double expenseAmount = 50 + (Math.random() * 1950);
                expense.setAmount(BigDecimal.valueOf((double) Math.round(expenseAmount * 100) / 100));
                expense.setDate(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(0, 30)));
                expense.setDescription(faker.lorem().sentence());

                ticket.setSubject(faker.lorem().sentence(3, 3));
                ticket.setDescription(faker.lorem().paragraph());
                ticket.setStatus(statuses[ThreadLocalRandom.current().nextInt(0, statuses.length)]);
                ticket.setPriority(priorities[ThreadLocalRandom.current().nextInt(0, priorities.length)]);
                ticket.setCustomer(customer);
                ticket.setManager(admin);
                ticket.setEmployee(admin);
                ticket.setCreatedAt(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(0, 30)));
                ticket.setExpense(expense);

                tickets.add(ticket);
            }
        }
        return tickets;
    }

    /**
     * Génère des leads aléatoires pour les clients existants
     *
     * @param customers Liste des clients
     * @param count     Nombre de leads à générer par client
     * @return Liste des leads générés
     */
    public List<Lead> generateRandomLeads(List<Customer> customers, int count) {
        User admin = userServiceImpl.findFirstByOrderByIdAsc();
        List<Lead> leads = new ArrayList<>();
        String[] statuses = { "meeting-to-schedule","scheduled","archived","success","assign-to-sales"};

        for (Customer customer : customers) {
            for (int i = 0; i < count; i++) {
                Lead lead = new Lead();

                Expense expense = new Expense();
                // Montant aléatoire entre 50 et 1000
                double expenseAmount = 50 + (Math.random() * 950);
                expense.setAmount(BigDecimal.valueOf((double) Math.round(expenseAmount * 100) / 100));
                expense.setDate(LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(0, 30)));
                expense.setDescription(faker.lorem().sentence());

                lead.setCustomer(customer);
                lead.setManager(admin);
                lead.setName(faker.company().name() + " Opportunity");
                lead.setEmployee(admin);
                lead.setStatus(statuses[ThreadLocalRandom.current().nextInt(0, statuses.length)]);
                lead.setCreatedAt(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(0, 60)));
                lead.setExpense(expense);

                leads.add(lead);
            }
        }
        return leads;
    }

    /**
     * Génère et sauvegarde des données aléatoires pour le CRM
     *
     * @param customerCount     Nombre de clients à générer
     * @param budgetPerCustomer Nombre de budgets par client
     * @param ticketPerCustomer Nombre de tickets par client
     * @param leadPerCustomer   Nombre de leads par client
     * @return Map contenant les statistiques des données générées
     * @throws SQLDataException si une erreur survient pendant la sauvegarde
     */
    @Transactional(rollbackFor = SQLDataException.class)
    public Map<String, Integer> generateAndSaveRandomData(int customerCount, int budgetPerCustomer,
                                                          int ticketPerCustomer, int leadPerCustomer) throws SQLDataException {
        try {
            // 1. Générer et sauvegarder les clients
            List<CustomerLoginInfo> customerLoginInfos = generateRandomCustomers(customerCount);
            List<Customer> customers = new ArrayList<>();

            for (CustomerLoginInfo customerLoginInfo : customerLoginInfos) {
                entityManager.persist(customerLoginInfo.getCustomer());
                entityManager.persist(customerLoginInfo);
                customers.add(customerLoginInfo.getCustomer());
            }

            // 2. Générer et sauvegarder les budgets
            List<Budget> budgets = generateRandomBudgets(customers, budgetPerCustomer);
            for (Budget budget : budgets) {
                entityManager.persist(budget);
            }

            // 3. Générer et sauvegarder les tickets
            List<Ticket> tickets = generateRandomTickets(customers, ticketPerCustomer);
            for (Ticket ticket : tickets) {
                entityManager.persist(ticket.getExpense());
                entityManager.persist(ticket);
            }

            // 4. Générer et sauvegarder les leads
            List<Lead> leads = generateRandomLeads(customers, leadPerCustomer);
            for (Lead lead : leads) {
                entityManager.persist(lead.getExpense());
                entityManager.persist(lead);
            }

            // 5. Retourner des statistiques sur les données générées
            Map<String, Integer> stats = new HashMap<>();
            stats.put("customers", customers.size());
            stats.put("budgets", budgets.size());
            stats.put("tickets", tickets.size());
            stats.put("leads", leads.size());

            return stats;

        } catch (Exception e) {
            throw new SQLDataException("Erreur lors de la génération de données aléatoires: " + e.getMessage());
        }
    }
}
