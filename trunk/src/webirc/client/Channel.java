package webirc.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Ayzen
 * @version 1.0 21.07.2006 15:58:13
 */
public class Channel {
  private String name;
  private Vector users;

  public Channel(String fullName) {
    if (fullName != null && fullName.length() > 1)
      name = fullName.substring(1);
    else
      name = "";
  }

  public Channel(String fullName, Collection users) {
    this(fullName);
    this.users = new Vector(users);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Iterator iterator() {
    if (users != null)
      return users.iterator();
    else
      return null;
  }

  public Collection getUsers() {
    return users;
  }

  public void setUsers(Collection users) {
    this.users = new Vector(users);
  }

  public int addUser(User user) {
    if (user == null)
      return -1;
    if (users == null)
      users = new Vector();
    if (!users.contains(user))
      return User.insertUser(users, user);
    return -1;
  }

  public void addUsers(Collection users) {
    if (users == null)
      return;
    if (this.users == null)
      this.users = new Vector();
    this.users.addAll(users);
  }

  public void removeUser(User user) {
    if (users != null)
      users.remove(user);
  }

  public void removeAllUsers() {
    if (users != null)
      users.clear();
  }

  public User getUser(User user) {
    User foundUser = null;
    for (Iterator it = users.iterator(); it.hasNext(); ) {
      foundUser = (User) it.next();
      if (foundUser.equals(user))
        break;
      else
        foundUser = null;
    }
    return foundUser;
  }


  public int hashCode() {
    return name.hashCode();
  }

  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof Channel) || ((Channel) obj).getName() == null)
      return false;

    return ((Channel) obj).getName().equalsIgnoreCase(name);
  }

  public String toString() {
    return '#' + name;
  }
}

