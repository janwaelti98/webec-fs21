package webeng.contactlist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.ContactRepository;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty("contact-list.add-sample-contacts")
public class SampleContactsAdder {

    private static final Logger logger = LoggerFactory.getLogger(SampleContactsAdder.class);

    public static final String JSON_FILE = "contacts.json";

    private final ObjectMapper mapper;
    private final ContactRepository contactRepo;

    public SampleContactsAdder(ObjectMapper mapper,
                               ContactRepository contactRepo) {
        this.mapper = mapper;
        this.contactRepo = contactRepo;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
        if (contactRepo.findAll().isEmpty()) {
            var sampleContacts = mapper.readValue(SampleContactsAdder.class.getResource(JSON_FILE),
                    new TypeReference<List<Contact>>() {});
            contactRepo.saveAll(sampleContacts);
            logger.info("Added {} sample contacts", sampleContacts.size());
        }
    }
}
