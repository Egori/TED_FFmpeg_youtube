package pageDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PageDAO {

    private final Connection con;

    public PageDAO(Connection con) {
        this.con = con;
    }

    //создание/обновление страницы в базе
    public void updatePage(Page page) {
        try {
            PreparedStatement ps;
            //если страница не новая
            if (page.getId() > 0) {
                ps = con.prepareStatement("UPDATE en SET link_ted=?, title=?, author=?, author_link=?, description=?, tags=?, index=?, filmed=?, media_id=?, transcript=? WHERE id=?");
                ps.setInt(11, page.getId());
            } else {
                ps = con.prepareStatement("INSERT INTO en (`link_ted`, `title`, `author`, `author_link`, `description`, `tags`, `index`, `filmed`, `media_id`, `transcript`) VALUES (?,?,?,?,?,?,?,?,?,?)");
            }
            
            ps.setString(1, page.getLinkTed());
            ps.setString(2, page.getTitle());
            ps.setString(3, page.getAuthor());
            ps.setString(4, page.getAuthorLink());
            ps.setString(5, page.getDescription());
            ps.setString(6, page.getTags());
            ps.setString(7, page.getIndex());
            ps.setString(8, page.getFilmed());
            ps.setString(9, page.getMediaId());
            ps.setString(10, page.getTranscript());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление
    public void deletePage(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM en WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastPage() {
        String result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM en WHERE `id`=(SELECT MAX(`id`) FROM en)");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("link_ted");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
