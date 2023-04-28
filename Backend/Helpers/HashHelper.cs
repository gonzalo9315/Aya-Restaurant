using Backend.Models.DataModels;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using System;

namespace Backend.Helpers
{
    public static class HashHelper
    {
        public static User HashedUsuario(User user)
        {
            Random rnd = new Random();
            byte[] b = new byte[128 / 8];
            rnd.NextBytes(b);
            user.Salt = Convert.ToBase64String(b);
            user.Password = Hashed(user.Password, user.Salt);
            return user;
        }

        private static string Hashed(string password, string salt)
        {
            string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                            password: password,
                            salt: Convert.FromBase64String(salt),
                            prf: KeyDerivationPrf.HMACSHA256,
                            iterationCount: 100000,
                            numBytesRequested: 256 / 8)
                            );
            return hashed;
        }

        public static bool Compare(string hash, string pass, string salt)
        {
            string hashedPass = Hashed(pass, salt);

            if(hash == hashedPass)
            {
                return true;
            }

            return false;
        }
    }
}
