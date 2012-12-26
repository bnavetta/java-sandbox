package com.bennavetta.sandbox;

import java.security.Policy;
import java.security.PermissionCollection;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.cert.Certificate;
import java.security.ProtectionDomain;
import java.security.CodeSource;
import java.security.AccessControlContext;
import java.security.AccessController;

import java.security.Permissions;

public class Sandbox
{
	private Sandbox() {}
	
	/**
	* Set up Java security so that the sandbox can enforce permissions. This method checks for a {@link SecurityManager}, and does nothing if one
	* is already installed. If there is no {@code SecurityManager}, then a {@link Policy} is installed that grants {@link AllPermission} to all code,
	* which is the effective behavior with no {@code SecurityManager} installed. Finally, a default {@code SecurityManager} is installed.
	* Note: You do not need to call this in an environment where Java security has already been configured. If you are having problems, try removing all
	* calls to this method and see what happens with the original security setup.
	*/
	public static void initSecurity()
	{
		if(System.getSecurityManager() == null)
		{
			// assume security hasn't been initialized
			
			Policy.setPolicy(new Policy(){
				@Override
					public PermissionCollection getPermissions(CodeSource cs)
					{
						return new AllPermission().newPermissionCollection(); //as far as I can tell, this is essentially what happens without a security manager
					}
			});
			// set a security manager so permissions get applied, 
			System.setSecurityManager(new SecurityManager());
		}
	}
	
	/**
	* Runs code in a sandbox. The actual permissions applied will be the ones contained in both the given {@link PermissionCollection} and 
	* the current context - you cannot grant permissions to the sandbox that you do not have.
	* @param perms the permissions to apply in the sandbox
	* @param action the action to run
	* @return the result of the sandboxed action
	* @see AccessController#doPrivileged(PrivilegedExceptionAction, AccessControlContext)
	*/
	public static <T> T run(PermissionCollection perms, PrivilegedExceptionAction<T> action) throws PrivilegedActionException
	{
		ProtectionDomain domain = new ProtectionDomain(new CodeSource(null, (Certificate[]) null), perms);
		AccessControlContext ctx = new AccessControlContext(new ProtectionDomain[]{ domain});
		return AccessController.doPrivileged(action, ctx);
	}
}