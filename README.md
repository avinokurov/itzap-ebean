# itzap-ebeans
**itzap-ebeans** provides an easy to use library for building reactive DAO with flexible schema models. 
### How To Build
* Clone the following projects: 
	* `git clone git@github.com:avinokurov/itzap-parent.git`
	* `git clone git@github.com:avinokurov/itzap-common.git`
	* `git clone git@github.com:avinokurov/itzap-rxjava.git`
	* `git clone git@github.com:avinokurov/itzap-ebeans.git`
* Build all projects
	* `cd itzap-parent && mvn clean install`
	* `cd ../itzap-common && mvn clean install`
	* `cd ../itzap-rxjava && mvn clean install`
	* `cd ../itzap-ebeans && mvn clean install`
* Example
	* `itzap-beans` project contains sample [UserDao](https://github.com/avinokurov/itzap-ebeans/blob/master/src/test/java/com/itzap/ebeans/test/dao/UserDao.java) implementation
