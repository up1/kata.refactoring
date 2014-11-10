package kata.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LDAPUtil {

	// //// Return Attributes ///////////////////
	private static String NAME = "name";
	private static String DEPARTMENT = "department";
	private static String COMPANY = "company";
	private static String MAIL = "mail";
	private static String MEMBEROF = "memberOf";
	private static String returnedAtts[] = { NAME, DEPARTMENT, COMPANY, MAIL, MEMBEROF, "EMPLOYEEID" };

	@SuppressWarnings("unchecked")
	public static Map queryAD(String username) throws AuthenticationException, NamingException, Exception {

		@SuppressWarnings("rawtypes")
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "xxx");
		env.put(Context.SECURITY_PRINCIPAL, "xxx");
		env.put(Context.SECURITY_CREDENTIALS, "xxx");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		Map amap = null;
		LdapContext ctxGC = null;
		String searchBase = "xxx";

		try {
			String searchFilter = "(sAMAccountName=" + username + ")";
			SearchControls searchCtls = new SearchControls();
			searchCtls.setReturningAttributes(returnedAtts);
			// logger.debug("----"+returnedAtts+"----");
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			ctxGC = new InitialLdapContext(env, null);
			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);

			SearchResult sr = (SearchResult) answer.next();

			Attributes attrs = sr.getAttributes();
			String key = "";
			Object value = "";
			if (attrs != null) {
				amap = new HashMap();
				NamingEnumeration ne = attrs.getAll();
				while (ne.hasMore()) {
					Attribute attr = (Attribute) ne.next();
					key = attr.getID();
					// set value
					if (attr.size() > 1) {
						ArrayList temps = new ArrayList();
						NamingEnumeration gg = attr.getAll();
						while (gg.hasMore()) {
							String temp = (String) gg.next();
							temps.add(temp);
						}
						value = temps;
					} else {
						value = attr.get();
					}
					amap.put(key, value);
				}
			}
		} catch (AuthenticationException ex) {
			throw (ex);
		} catch (NamingException ex) {
			throw (ex);
		} catch (Exception ex) {
			throw (ex);
		}

		return amap;
	}

	@SuppressWarnings("unchecked")
	public static Map queryAD(String username, String pwd, String domainUrl, String LdapPriUrl, String LdapSecUrl, String domainSearchBase) throws AuthenticationException, NamingException, Exception {

		@SuppressWarnings("rawtypes")
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + LdapPriUrl + " ldap://" + LdapSecUrl);
		env.put(Context.SECURITY_PRINCIPAL, username + "@" + domainUrl);
		env.put(Context.SECURITY_CREDENTIALS, pwd);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put("com.sun.jndi.ldap.read.timeout", "2000");
		Map amap = null;
		// LdapContext ctxGC = null;
		String searchBase = domainSearchBase;

		try {

			String searchFilter = "(sAMAccountName=" + username + ")";
			SearchControls searchCtls = new SearchControls();
			// searchCtls.setReturningAttributes(returnedAtts);
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// ctxGC = new InitialLdapContext(env, null);
			DirContext ctxGC = new InitialDirContext(env);

			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);

			SearchResult sr = (SearchResult) answer.next();

			Attributes attrs = sr.getAttributes();
			String key = "";
			Object value = "";
			if (attrs != null) {
				amap = new HashMap();
				NamingEnumeration ne = attrs.getAll();
				while (ne.hasMore()) {
					Attribute attr = (Attribute) ne.next();
					key = attr.getID();
					// set value
					if (attr.size() > 1) {
						ArrayList temps = new ArrayList();
						NamingEnumeration gg = attr.getAll();
						while (gg.hasMore()) {
							String temp = (String) gg.next();
							temps.add(temp);
						}
						value = temps;
					} else {
						value = attr.get();
					}
					amap.put(key, value);
					// logger.debug("===="+key+","+value);
				}
			}
		} catch (AuthenticationException ex) {
			throw (ex);
		} catch (NamingException ex) {
			throw (ex);
		} catch (Exception ex) {
			throw (ex);
		}

		return amap;
	}
}