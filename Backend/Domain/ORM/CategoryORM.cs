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
    public class CategoryORM : ICategoryORM
    {
        private MySQLConfiguration _connectionString;

        public CategoryORM(MySQLConfiguration connectionString)
        {
            _connectionString = connectionString;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public async Task<bool> Create(Category category)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO categories (name, image, createdAt) VALUES (@Name, @Image, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { category.Name, category.Image, category.CreatedAt });
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

        public async Task<IEnumerable<Category>> GetAll()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories";
            return await db.QueryAsync<Category>(sql, new { });
        }
        public async Task<IEnumerable<Category>> GetAllAvalaible()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories WHERE isDeleted=false";
            return await db.QueryAsync<Category>(sql, new { });
        }

        public async Task<Category> GetByID(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM categories WHERE id=@id";
            return await db.QuerySingleAsync<Category>(sql, new { id });
        }

        public async Task<bool> UpdateByID(int id, Category category)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE categories SET name=@Name, updatedAt=@updatedAt WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new { category.Name, updatedAt, id });
            return result > 0;
        }
        public async Task<bool> AddDish(DishCategory dishCategory)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO dishes_category (categorieId, dishId) VALUES (@CategoryId, @DishId)";
            var result = await db.ExecuteAsync(sql, new { dishCategory.CategoryId, dishCategory.DishId });
            return result > 0;
        }

        public async Task<bool> DeleteDish(DishCategory dishCategory)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE dishes_category 
                        SET isDeleted=true, deletedAt=@deletedAt 
                        WHERE categorieId=@categorieId AND dishId=@dishId";
            var result = await db.ExecuteAsync(sql, new { deletedAt, dishCategory.CategoryId, dishCategory.DishId });
            return result > 0;
        }
    }
}
