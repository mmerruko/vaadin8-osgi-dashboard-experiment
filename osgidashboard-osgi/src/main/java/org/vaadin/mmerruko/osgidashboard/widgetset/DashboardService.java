package org.vaadin.mmerruko.osgidashboard.widgetset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.vaadin.mmerruko.griddashboard.model.GridDashboardModel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(service = DashboardService.class)
public class DashboardService {
    private static final String JSON = ".json";
    public static final String LOCATION_PROPERTY = "dashboard.model.location";
    private Path location;

    @Activate
    void setup() {
        location = Paths.get(System.getProperty("user.home"), ".dashboards");
        try {
            Files.createDirectories(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDashboard(GridDashboardModel model, String name) {
        Path modelPath = location.resolve(name + JSON);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(modelPath.toFile(), model);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public GridDashboardModel loadDashboard(String name) {
        Path modelPath = location.resolve(name + JSON);
        try (InputStream in = Files.newInputStream(modelPath)) {
            GridDashboardModel model = new ObjectMapper().readValue(in,
                    GridDashboardModel.class);
            return model;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<String> getAvailableDashboards() {
        ArrayList<String> storedDashboards = new ArrayList<>();
        try {
            Files.walkFileTree(location, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attrs) throws IOException {
                    String name = stripJSONExtension(file);
                    storedDashboards.add(name);

                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storedDashboards;
    }

    protected String stripJSONExtension(Path file) {
        String name = file.getFileName().toString();
        if (name.toLowerCase().endsWith(JSON)) {
            return name.substring(0, name.length() - JSON.length());
        }
        return name;
    }

}
