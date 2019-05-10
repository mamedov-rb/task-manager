package ru.rmamedov.app.model.user.meta;

import ru.rmamedov.app.model.user.Role;
import ru.rmamedov.app.model.user.User;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author Rustam Mamedov
 */

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {
    public static volatile SingularAttribute<User, String> id;
    public static volatile SingularAttribute<User, String> username;
    public static volatile SetAttribute<User, Role> roles;
}
