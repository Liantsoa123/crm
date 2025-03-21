package site.easy.to.build.crm.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SystemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetData() {
        // Your SQL script as native query
        String resetScript = """
                -- Create temporary table for manager users
                CREATE TEMPORARY TABLE manager_users AS 
                SELECT u.id FROM users u 
                JOIN user_roles ur ON u.id = ur.user_id 
                JOIN roles r ON ur.role_id = r.id 
                WHERE r.name = 'ROLE_MANAGER';
                
                SET FOREIGN_KEY_CHECKS = 0;
                
                DELETE FROM google_drive_file;
                DELETE FROM file;
                DELETE FROM trigger_contract;
                DELETE FROM contract_settings WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM lead_action;
                DELETE FROM trigger_lead;
                DELETE FROM lead_settings WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM trigger_ticket;
                DELETE FROM ticket_settings WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM email_template WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM customer;
                DELETE FROM oauth_users WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM user_profile WHERE user_id NOT IN (SELECT id FROM manager_users);
                DELETE FROM user_roles WHERE user_id NOT IN (SELECT id FROM manager_users) OR role_id NOT IN (SELECT role_id FROM user_roles ur JOIN manager_users mu ON ur.user_id = mu.id);
                DELETE FROM customer_login_info;
                DELETE FROM users WHERE id NOT IN (SELECT id FROM manager_users);
                
                ALTER TABLE customer_login_info AUTO_INCREMENT = 1;
                ALTER TABLE employee AUTO_INCREMENT = 1;
                ALTER TABLE roles AUTO_INCREMENT = 1;
                ALTER TABLE users AUTO_INCREMENT = 1;
                ALTER TABLE customer AUTO_INCREMENT = 1;
                ALTER TABLE email_template AUTO_INCREMENT = 1;
                ALTER TABLE contract_settings AUTO_INCREMENT = 1;
                ALTER TABLE lead_settings AUTO_INCREMENT = 1;
                ALTER TABLE oauth_users AUTO_INCREMENT = 1;
                ALTER TABLE ticket_settings AUTO_INCREMENT = 1;
                ALTER TABLE trigger_lead AUTO_INCREMENT = 1;
                ALTER TABLE lead_action AUTO_INCREMENT = 1;
                ALTER TABLE trigger_contract AUTO_INCREMENT = 1;
                ALTER TABLE file AUTO_INCREMENT = 1;
                ALTER TABLE google_drive_file AUTO_INCREMENT = 1;
                ALTER TABLE trigger_ticket AUTO_INCREMENT = 1;
                ALTER TABLE user_profile AUTO_INCREMENT = 1;
                
                SET FOREIGN_KEY_CHECKS = 1;
                
                DROP TEMPORARY TABLE manager_users;
                """;

        entityManager.createNativeQuery(resetScript).executeUpdate();
    }
}