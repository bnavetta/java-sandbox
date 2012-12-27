package com.bennavetta.sandbox;

import java.security.*;

/**
* Assorted utility functions helpful when dealing with permissions.
*/
public class PermissionUtils
{
	private PermissionUtils() {}
	
	// can't test these because they will be different for more or less any OS, VM, or configuration
	
	/**
	* Determine the permissions granted to a given object based on the current {@link Policy}.
	* @param obj the object for whom permissions will be determined
	* @return the {@code PermissionCollection} associated with the given object's class by the current {@code Policy}
	* @see #getPermissionsFor(java.lang.Class)
	*/
	public static PermissionCollection getPermissionsFor(Object obj)
	{
		return Policy.getPolicy().getPermissions(obj.getClass().getProtectionDomain().getCodeSource());
	}
	
	/**
	* Determine the permissions granted to a given class based on the current {@link Policy}.
	* @param clazz the class to use
	* @return the {@code PermissionCollection} associated with the given class by the current {@code Policy}
	* @see #getPermissionsFor(Object)
	*/
	public static PermissionCollection getPermissionsFor(Class clazz)
	{
		return Policy.getPolicy().getPermissions(clazz.getProtectionDomain().getCodeSource());
	}	
}