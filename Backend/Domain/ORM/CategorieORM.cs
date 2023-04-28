using Backend.DataAccess;
using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;
using Dapper;
using MySql.Data.MySqlClient;
using System;

namespace Backend.Domain.ORM
{
    public class CategorieORM : ICategorieORM
    {
        private MySQLConfiguration _connectionString;

        public CategorieORM(MySQLConfiguration connectionString)
        {
            _connectionString = connectionString;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public async Task<bool> Create(Categorie categorie)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO categories (name, createdAt) VALUES (@Name, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { categorie.Name, categorie.CreatedAt });
            return result > 0;
        }

        public async Task<bool> DeleteByID(int id)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE categories SET isDeleted=true, deletedAt=@deletedAt where id=@id";
            var result = await db.ExecuteAsync(sql, new { deletedAt, id });
            return result > 0;
        }

        public async Task<IEnumerable<Categorie>> GetAll()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories";
            return await db.QueryAsync<Categorie>(sql, new { });
        }
        public async Task<IEnumerable<Categorie>> GetAllAvalaible()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories WHERE isDeleted=false";
            return await db.QueryAsync<Categorie>(sql, new { });
        }

        public async Task<Categorie> GetByID(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories WHERE id=@id";
            return await db.QuerySingleAsync<Categorie>(sql, new { id });
        }

        public async Task<bool> UpdateByID(int id, Categorie categorie)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE categories SET name=@Name, updatedAt=@updatedAt WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new { categorie.Name, updatedAt, id });
            return result > 0;
        }
        public async Task<bool> AddDish(DishCategorie dishCategorie)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO dishes_categorie (categorieId, dishId) VALUES (@CategorieId, @DishId)";
            var result = await db.ExecuteAsync(sql, new { dishCategorie.CategorieId, dishCategorie.DishId });
            return result > 0;
        }

        public async Task<bool> DeleteDish(DishCategorie dishCategorie)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE dishes_categorie 
                        SET isDeleted=true, deletedAt=@deletedAt 
                        WHERE categorieId=@categorieId AND dishId=@dishId";
            var result = await db.ExecuteAsync(sql, new { deletedAt, dishCategorie.CategorieId, dishCategorie.DishId });
            return result > 0;
        }
    }
}
