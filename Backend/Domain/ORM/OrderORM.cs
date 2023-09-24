using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;
using Dapper;
using Backend.DataAccess;
using MySql.Data.MySqlClient;
using System;
using Backend.Models;

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

        /*public async Task<Order> Create(Order order)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO orders (userId, address, createdAt) VALUES (@UserId, @Address, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { order.UserId, order.Address, order.CreatedAt });
            return result > 0;
        }*/
        public async Task<Order> Create(Order order)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO orders (userId, address, createdAt) VALUES (@UserId, @Address, @CreatedAt)";
            var result = await db.ExecuteAsync(sql, new { order.UserId, order.Address, order.CreatedAt });

            if (result > 0)
            {
                sql = "SELECT * FROM orders WHERE id = LAST_INSERT_ID()";
                var insertedOrder = await db.QueryFirstOrDefaultAsync<Order>(sql);

                return insertedOrder;
            }
            else
            {
                throw new Exception("Failed to create the order");
            }
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

        public async Task<IEnumerable<Order>> GetAllNew()
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM orders where state!='delivered'";
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

        public async Task<Order> GetMyNewOrder(int idUser)
        {
            var db = DbConnection();
            var sql = @"SELECT * FROM orders WHERE userId=@idUser ORDER BY id desc limit 1";
            return await db.QuerySingleAsync<Order>(sql, new { idUser });
        }

        public async Task<IEnumerable<DishAmount>> GetDishesOfOrder(int id)
        {
            var db = DbConnection();
            var sql = @"SELECT dishes.*, order_items.amount FROM dishes 
                        INNER JOIN order_items ON dishes.id=order_items.dishId 
                        WHERE orderId=@id";
            return await db.QueryAsync<DishAmount>(sql, new { id });
        }

        public async Task<bool> UpdateByID(int id, Order order)
        {
            var db = DbConnection();
            DateTime updatedAt = DateTime.Now;
            var sql = @"UPDATE orders SET state=@State, updatedAt=@updatedAt WHERE id=@id";
            var result = await db.ExecuteAsync(sql, new { order.State, updatedAt, id });
            return result > 0;
        }

        public async Task<bool> AddItem(OrderItem Orderitem)
        {
            var db = DbConnection();
            var sql = @"INSERT INTO order_items (orderId, dishId, amount) VALUES (@OrderId, @DishId, @Amount)";
            var result = await db.ExecuteAsync(sql, new { Orderitem.OrderId, Orderitem.DishId, Orderitem.Amount });
            return result > 0;
        }
    }
}
