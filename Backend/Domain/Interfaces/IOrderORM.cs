using Backend.Models;
using Backend.Models.DataModels;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Backend.Domain.Interfaces
{
    public interface IOrderORM
    {
        Task<IEnumerable<Order>> GetAll();
        Task<IEnumerable<Order>> GetAllNew();
        Task<IEnumerable<Order>> GetMyOrders(int idUser);
        Task<Order> GetMyNewOrder(int id);
        Task<IEnumerable<DishAmount>> GetDishesOfOrder(int id);
        Task<Order> GetByID(int id);
        Task<Order> Create(Order order);
        Task<bool> AddItem(OrderItem orderItem);
        Task<bool> DeleteByID(int id);
        Task<bool> UpdateByID(int id, Order order);
    }
}
