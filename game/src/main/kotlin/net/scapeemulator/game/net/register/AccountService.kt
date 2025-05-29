//package net.scapeemulator.game.net.register
//
//import org.mindrot.jbcrypt.BCrypt
//
//class AccountService @Inject constructor(
//    private val accountRepository: AccountRepository
//) {
//    fun createAccount(request: CreateAccountRequest): Account? {
//        val (username, email, password) = request
//
//        if (accountRepository.findAccountByUsername(username) != null) return null
//
//        return accountRepository.createAccount(
//            Account(
//                userName = username,
//                rights = 0,
//                email = email,
//                password = BCrypt.hashpw(password, BCrypt.gensalt(12)),
//                location = Location.Default
//            )
//        )
//    }
//
//    fun findAccountByUsername(username: String): Account? = accountRepository.findAccountByUsername(username)
//
//    fun validateAccount(plainText: String, password: String): Boolean = // TODO: Can add a validation response, depending on if the account has been banned etc.
//        BCrypt.checkpw(plainText, password)
//
//    fun saveAccount(updateAccountRequest: UpdateAccountRequest): Boolean = accountRepository.saveAccount(updateAccountRequest)
//}
