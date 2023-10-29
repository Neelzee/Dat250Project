package no.haugalandplus.val;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PollRepository pollRepository;

    private Random random = new Random(42);
    public User saveNewUser() {
        User user = new User();
        user.setUsername("Guro Victoria" + random.nextInt());
        user.setPassword("Dette er et rop om help");
        return saveNewUser(user);
    }

    public User saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void setSecurityContextUser() {
        setSecurityContextUser(saveNewUser());
    }

    public void setSecurityContextUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
    }

    public Poll saveNewPoll() {
        User u = saveNewUser();
        return saveNewPoll(u);
    }

    public Poll saveNewPoll(User u) {
        Poll poll = new Poll();
        poll.setUser(u);
        poll.setNeedLogin(false);
        poll.setChoices(new ArrayList<>());
        poll.setName("Is this a good question?");
        poll.setDescription("Lalala");
        poll.setStatus(PollStatusEnum.NOT_INITIALISED);
        return pollRepository.save(poll);
    }

    public Poll addChoices(Poll poll) {
        List<Choice> choiceList = new ArrayList<>();
        Choice choice1 = new Choice();
        choice1.setName("Yes");
        choice1.setDescription("No");
        choiceList.add(choice1);

        Choice choice2 = new Choice();
        choice2.setName("No");
        choice2.setDescription("Yes");
        choiceList.add(choice2);

        poll.setChoices(choiceList);

        poll = pollRepository.save(poll);
        assertThat(poll, notNullValue());
        assertThat(poll.getChoices(), hasSize(2));

        return poll;
    }
}