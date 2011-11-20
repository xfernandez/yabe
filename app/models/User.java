package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "Sblog_user")
public class User extends Model
{

    @Email
    @Required
    public String email;

    @Required
    public String password;

    public String fullname;

    public boolean isAdmin;

    public User(final String email, final String password, final String fullname)
    {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }

    public static User connect(final String email, final String password)
    {
        return find("byEmailAndPassword", email, password).first();
    }

    @Override
    public String toString()
    {
        return email;
    }

    static boolean authenticate(final String username, final String password)
    {
        return User.connect(username, password) != null;
    }

}
