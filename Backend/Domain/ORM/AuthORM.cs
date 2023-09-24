using Backend.DataAccess;
using Backend.Domain.Interfaces;
using Backend.Helpers;
using Backend.Models;
using Backend.Models.DataModels;
using Dapper;
using MySql.Data.MySqlClient;
using System;
using System.Threading.Tasks;

namespace Backend.Domain.ORM
{
    public class AuthORM : IAuthORM
    {
        private MySQLConfiguration _connectionString;
        private readonly JwtSettings _jwtSettings;

        public AuthORM(MySQLConfiguration connectionString, JwtSettings jwtSettings)
        {
            _connectionString = connectionString;
            _jwtSettings = jwtSettings;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public UserTokens Login(string email, string password)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM users WHERE email=@Email";
            var userFound = db.QuerySingle<User>(sql, new { email });

            if(userFound != null && userFound.IsDeleted == false)
            {
                bool verifyPass = HashHelper.Compare(userFound.Password, password, userFound.Salt);
                if (!verifyPass) throw new Exception("Wrong Password");

                var token = JwtHelper.GetTokenKey(new UserTokens()
                {
                    Username = userFound.Name,
                    Id = userFound.Id,
                    Role = userFound.Role,
                    GuidId = Guid.NewGuid()
                }, _jwtSettings);

                return token;
            }
            else
            {
                throw new Exception("User doesn't exist");
            }
        }

        public async Task<bool> Register(User user)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO users (name, email, password, address, phone, createdAt, salt)
                        VALUES (@Name, @Email, @Password, @Address, @Phone, @CreatedAt, @Salt)";
            var result = await db.ExecuteAsync(sql, new {   user.Name,
                                                            user.Email,
                                                            user.Password,
                                                            user.Address,
                                                            user.Phone,
                                                            user.CreatedAt,
                                                            user.Salt
                                                        });
            return result > 0;
        }

        public async Task<bool> Update(int id, User user)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE users 
                        SET name=@Name, email=@Email, password=@Password, address=@Address, phone=@Phone, updatedAt=@updatedAt 
                        WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new {   user.Name,
                                                            user.Email,
                                                            user.Password,
                                                            user.Address,
                                                            user.Phone,
                                                            updatedAt,
                                                            id
                                                        });
            return result > 0;
        }

        public async Task<User> Profile(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM users WHERE id=@id";
            var user = await db.QuerySingleAsync<User>(sql, new { id });
            return user;
        }

        public async Task<bool> Delete(int id)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE users SET isDeleted=true, deletedAt=@deletedAt where id=@id";
            var result = await db.ExecuteAsync(sql, new { deletedAt, id });
            return result > 0;
        }
    }
}
