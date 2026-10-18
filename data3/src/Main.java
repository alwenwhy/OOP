import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();

    private static final Set<String> visited = ConcurrentHashMap.newKeySet();
    private static final List<String> messages = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger pending = new AtomicInteger(0);
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    static class ParsedResponse {
        String message;
        List<String> successors;
    }



    public static void main(String[] args) throws Exception {
        visited.add("/");
        pending.incrementAndGet();
        executor.submit(() -> check("/"));

        synchronized (pending) {
            while (pending.get() > 0) {
                pending.wait(1000);
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        List<String> sorted = new ArrayList<>(messages);
        Collections.sort(sorted);
        for (String m : sorted) {
            System.out.println(m);
        }
    }



    private static void check(String path) {
        try {
            String json = fetch(path);
            ParsedResponse resp = parseJson(json);

            if (resp.message != null)
                messages.add(resp.message);

            if (resp.successors != null) {
                for (String succ : resp.successors) {
                    if (visited.add(succ)) {
                        pending.incrementAndGet();
                        executor.submit(() -> check(succ));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error [" + path + "]: " + e.getMessage());
        } finally {
            if (pending.decrementAndGet() == 0) {
                synchronized (pending) {
                    pending.notifyAll();
                }
            }
        }
    }

    private static String fetch(String path) throws Exception {
        String url = buildUrl(path);
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(60)).GET().build();
        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200)
            throw new RuntimeException("HTTP " + resp.statusCode() + " for " + url);

        return resp.body();
    }

    private static String buildUrl(String path) {
        if (path == null || path.isEmpty())
            path = "/";

        if (!path.startsWith("/"))
            path = "/" + path;

        return BASE_URL + path;
    }


    private static ParsedResponse parseJson(String json) {
        ParsedResponse r = new ParsedResponse();
        int i = 0;
        int n = json.length();


        while (i < n) {
            if (json.startsWith("\"message\"", i)) {
                i += "\"message\"".length();

                i = skip(json, i);
                StringBuilder sb = new StringBuilder();
                i = read(json, i, sb);

                r.message = sb.toString();
            } else if (json.startsWith("\"successors\"", i)) {
                i += "\"successors\"".length();

                i = skip(json, i);
                if (i < n && json.charAt(i) == '[') {
                    i++;
                    List<String> list = new ArrayList<>();

                    while (i < n && json.charAt(i) != ']') {
                        if (json.charAt(i) == '"') {
                            StringBuilder sb = new StringBuilder();
                            i = read(json, i, sb);

                            list.add(sb.toString());
                        } else {
                            i++;
                        }
                    }

                    r.successors = list;
                }
            } else {
                i++;
            }
        }

        return r;
    }

    private static int skip(String s, int i) {
        while (i < s.length() && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ':')) {
            i++;
        }

        return i;
    }

    private static int read(String s, int i, StringBuilder out) {
        if (i >= s.length() || s.charAt(i) != '"')
            return i;

        i++;
        while (i < s.length()) {
            char c = s.charAt(i);

            if (c == '\\') {
                i++;

                if (i >= s.length())
                    break;

                char e = s.charAt(i);
                switch (e) {
                    case '"':  out.append('"');  i++; break;
                    case '\\': out.append('\\'); i++; break;
                    case '/':  out.append('/');  i++; break;
                    case 'n':  out.append('\n'); i++; break;
                    case 't':  out.append('\t'); i++; break;
                    case 'r':  out.append('\r'); i++; break;
                    case 'b':  out.append('\b'); i++; break;
                    case 'f':  out.append('\f'); i++; break;
                    case 'u':
                        if (i + 4 < s.length()) {
                            String hex = s.substring(i + 1, i + 5);
                            out.append((char) Integer.parseInt(hex, 16));
                            i += 5;
                        } else {
                            i++;
                        }
                        break;
                    default:
                        out.append(e);
                        i++;
                }
            } else if (c == '"') {
                return i + 1;
            } else {
                out.append(c);
                i++;
            }
        }
        return i;
    }
}