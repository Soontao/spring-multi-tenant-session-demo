package org.fornever.demo.multitenantsession.config.internal;

import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionRepository implements SessionRepository {

    private final static String INTERNAL = "__INTERNAL__";
    private final static String ZONE_ID = "__ZONE_ID__";
    private final static String TMP_SESSION_ZONE = "__TMP__";
    private final static String OWN_REPO = "__OWN_REPO__";

    private final static String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public InMemorySessionRepository() {
        this.repos = new ConcurrentHashMap<>();
    }

    private Map<String, SessionRepository> repos;

    public SessionRepository getRepo() {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        String zoneId = null;
        if (attributes != null) {
            zoneId = (String) attributes.getAttribute(ZONE_ID, 0);
        }
        if (zoneId == null) {
            zoneId = TMP_SESSION_ZONE;
        }
        return getRepo(zoneId);

    }

    public SessionRepository getRepo(String zoneId) {
        if (!this.repos.containsKey(zoneId)) {
            this.repos.put(zoneId, new MapSessionRepository(new ConcurrentHashMap<>()));
        }
        return this.repos.get(zoneId);
    }

    public Session findById(String id) {
        Session session = getRepo().findById(id);
        return session;
    }

    public void deleteById(String s) {
        getRepo().deleteById(s);
    }

    public Session createSession() {
        SessionRepository repo = getRepo(TMP_SESSION_ZONE);
        Session session = repo.createSession();
        session.setAttribute(OWN_REPO, repo);
        return session;
    }

    public void save(Session session) {
        String zoneId = session.getAttribute(ZONE_ID);
        SessionRepository defaultRepo = getRepo(TMP_SESSION_ZONE);
        SessionRepository oldRepo = session.getAttribute(OWN_REPO);
        SessionRepository newRepo = getRepo(zoneId);
        if (oldRepo != newRepo && newRepo != defaultRepo) {
            oldRepo.deleteById(session.getId());
            session.setAttribute(OWN_REPO, newRepo);
        }
        newRepo.save(session);
    }
}
