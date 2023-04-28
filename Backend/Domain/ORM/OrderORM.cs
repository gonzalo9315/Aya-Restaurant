using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;
using Dapper;
using Backend.DataAccess;
using MySql.Data.MySqlClient;
using System;

namespace Backend.Domain.ORM
{
    public class OrderORM : IOrderORM
    {
        private MySQLConfiguration _connectionString;

        public OrderORM(MySQLConfiguration connectionString)
        {
            _connectionString = connectionString;
        }

        protected MySqlConnection DbConnection()
        {
            return new MySqlConnection(_connectionString.ConnectionString);
        }

        public async Task<bool> Create(Order order)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO orders (userId, address, createdAt) VALUES (@UserId, @Address, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { order.UserId, order.Address, order.CreatedAt });
            return result > 0;
        }

        public async Task<bool> DeleteByID(int id)
        {
            var db = DbConnection();
            DateTime deletedAt = DateTime.Now;
            var sql = @"UPDATE orders SET isDeleted=true, deletedAt=@deletedAt where id=@id";
            var result = await db.ExecuteAsync(sql, new { deletedAt, id });
            return result > 0;
        }

        public async Task<IEnumerable<Order>> GetAll()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM orders";
            return await db.QueryAsync<Order>(sql, new { });
        }

        public async Task<Order> GetByID(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM orders WHERE id=@id";
            return await db.QuerySingleAsync<Order>(sql, new { id });
        }

        public async Task<IEnumerable<Order>> GetMyOrders(int idUser)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM orders WHERE userId=@idUser";
            return await db.QueryAsync<Order>(sql, new { idUser });
        }

        public async Task<IEnumerable<Dish>> GetDishesOfOrder(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM dishes 
                        INNER JOIN order_items ON dishes.id=order_items.dishId 
                        WHERE orderId=@id";
            return await db.QueryAsync<Dish>(sql, new { id });
        }

        public async Task<bool> UpdateByID(int id, Order order)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE orders SET name=@Name, updatedAt=@updatedAt WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new { order.State, updatedAt, id });
            return result > 0;
        }

        public async Task<bool> AddItem(OrderItem Orderitem)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO order_items (orderId, dishId) VALUES (@OrderId, @DishId)";
            var result = await db.ExecuteAsync(sql, new { Orderitem.OrderId, Orderitem.DishId });
            return result > 0;
        }
    }
}
