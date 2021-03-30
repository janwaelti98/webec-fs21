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
import webeng.contactlist.service.ContactService;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty("contact-list.add-sample-contacts")
public class SampleContactsAdder {

    public static final String JSON_FILE = "contacts.json";

    private static final Logger logger = LoggerFactory.getLogger(SampleContactsAdder.class);

    private final ObjectMapper mapper;
    private final ContactService contactService;

    public SampleContactsAdder(ObjectMapper mapper,
                               ContactService contactService) {
        this.mapper = mapper;
        this.contactService = contactService;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
        addSampleContacts();
    }

    public void addSampleContacts() throws IOException {
        if (contactService.getContactList(null).isEmpty()) {
            logger.info("Adding sample contacts");
            var sampleContacts = mapper.readValue(SampleContactsAdder.class.getResource(JSON_FILE),
                    new TypeReference<List<Contact>>() {});
            sampleContacts.forEach(contactService::add);
        }
    }
}
