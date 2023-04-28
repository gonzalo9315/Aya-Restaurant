using Backend.DataAccess;
using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.Threading.Tasks;
using Dapper;
using System;

namespace Backend.Domain.ORM
{
    public class DishORM : IDishORM
    {
        private MySQLConfiguration _connectionString;

        public DishORM(MySQLConfiguration connectionString)
        {
            _connectionString = connectionString;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public async Task<bool> Create(Dish dish)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO dishes (name, description, ingredients, photo, createdAt) 
                        VALUES (@Name, @Description, @Ingredients, @Photo, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { dish.Name, dish.Description, dish.Ingredients, dish.Photo, dish.CreatedAt });
            return result > 0;
        }

        public async Task<bool> DeleteByID(int id)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE dishes SET isDeleted=true, deletedAt=@deletedAt where id=@id";
            var result = await db.ExecuteAsync(sql, new { deletedAt, id });
            return result > 0;
        }

        public async Task<IEnumerable<Dish>> GetAllByCategorie(int idCategorie)
        {
            var db = DbConnection();
            var sql1 = @"SELECT * FROM categories WHERE id=@idCategorie";
            var categorieIsDeleted = await db.QuerySingleAsync<Categorie>(sql1, new { idCategorie });

            if(categorieIsDeleted.IsDeleted)
            {
                return new List<Dish>();
            }
            else
            {
                var sql2 = @"SELECT * FROM dishes 
                            INNER JOIN dishes_categorie ON dishes.id=dishes_categorie.dishId 
                            WHERE categorieId=@idCategorie AND dishes_categorie.isDeleted=false";
                return await db.QueryAsync<Dish>(sql2, new { idCategorie });
            }
        }

        public async Task<IEnumerable<Dish>> GetAll()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM dishes";
            return await db.QueryAsync<Dish>(sql, new { });
        }

        public async Task<Dish> GetByID(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM dishes WHERE id=@id";
            return await db.QuerySingleAsync<Dish>(sql, new { id });
        }

        public async Task<bool> UpdateByID(int id, Dish dish)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE dishes 
                        SET name=@Name, description=@Description, ingredients=@Ingredients, photo=@Photo, updatedAt=@updatedAt 
                        WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new { dish.Name, dish.Description, dish.Ingredients, dish.Photo, updatedAt, id });
            return result > 0;
        }

    }
}
