/* Status
 * Simple enum to store various status codes. Allows for servlets to
 * alter response based on status code.
 */
public enum Status {
	OK, 				// no errors occurred
	ERROR, 				// generic error occurred
	INVALID_CONFIG, 	// configuration file was invalid
	NO_CONFIG, 			// configuration file not found
	NO_DRIVER, 			// driver not found
	CONNECTION_FAILED, 	// failed to establish connection
	SQL_ERROR, 			// sql error/exception occurred
	DUPLICATE_USER, 	// duplicate users detected
	PASSWORD_LENGTH,	// password length too short/long
	NULL_VALUES			// null values detected
}
