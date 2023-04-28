using Backend.DataAccess;
using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using Dapper;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.ORM
{
    public class UserORM: IUserORM
    {
        private MySQLConfiguration _connectionString;

        public UserORM(MySQLConfiguration connectionString)
        {
            _connectionString = connectionString;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public async Task<bool> Create(User user)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO users (name, email, password, address, phone, role, createdAt, salt)
                        VALUES (@Name, @Email, @Password, @Address, @Phone, @Role, @CreatedAt, @Salt)";
            var result = await db.ExecuteAsync(sql, new {   user.Name, 
                                                            user.Email, 
                                                            user.Password, 
                                                            user.Address, 
                                                            user.Phone, 
                                                            user.Role, 
                                                            user.CreatedAt, 
                                                            user.Salt 
                                                        });
            return result > 0;
        }

        public async Task<bool> DeleteByID(int id)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE users SET isDeleted=true, deletedAt=@deletedAt where id=@id";
            var result = await db.ExecuteAsync(sql, new { deletedAt, id });
            return result > 0;
        }

        public async Task<bool> UpdateByID(int id, User user)
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

        public async Task<IEnumerable<User>> GetAll()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM users";
            return await db.QueryAsync<User>(sql, new { });
        }

        public async Task<User> GetByID(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM users WHERE id=@id";
            return await db.QuerySingleAsync<User>(sql, new { id });
        }
    }
}
