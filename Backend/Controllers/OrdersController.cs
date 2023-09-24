using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System;
using System.Linq;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Backend.Domain.ORM;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrdersController: Controller
    {
        private readonly IOrderORM _orderORM;

        public OrdersController(IOrderORM orderORM)
        {
            _orderORM = orderORM;
        }

        // GET: api/Orders
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin, Employee")]
        public async Task<ActionResult> GetAllOrders()
        {
            var orders = await _orderORM.GetAll();

            if (orders.LongCount() > 0)
            {
                return Ok(orders);
            }
            else
            {
                return BadRequest("Error in Get Orders: There is no orders");
            }
        }

        // GET: api/Orders/NewOrders
        [Route("NewOrders")]
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin, Employee")]
        public async Task<ActionResult> GetAllNewOrders()
        {
            var orders = await _orderORM.GetAllNew();

            if (orders.LongCount() > 0)
            {
                return Ok(orders);
            }
            else
            {
                return BadRequest("Error in Get New Orders: There is no orders");
            }
        }

        // GET: api/Orders/MyOrders
        [Route("MyOrders")]
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> GetAllIMyOrders()
        {
            var idToken = Int32.Parse(HttpContext.User!
                                     .Claims!
                                     .FirstOrDefault(c => c.Type == "id")!
                                     .Value);
            var orders = await _orderORM.GetMyOrders(idToken);

            if (orders.LongCount() > 0)
            {
                return Ok(orders);
            }
            else
            {
                return BadRequest("Error in Get My Orders: There is no orders");
            }
        }

        // GET: api/Orders/MyNewOrder
        [Route("MyNewOrder")]
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> GetMyNewOrder()
        {
            var idToken = Int32.Parse(HttpContext.User!
                         .Claims!
                         .FirstOrDefault(c => c.Type == "id")!
                         .Value);

            try
            {
                return Ok(await _orderORM.GetMyNewOrder(idToken));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get My New Order by ID: " + ex.Message);
            }
        }

        // GET: api/Orders/5/Items
        [Route("{id}/items")]
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> GetAllItemsOfOrder(int id)
        {
            var orderItems = await _orderORM.GetDishesOfOrder(id);

            if (orderItems.LongCount() > 0)
            {
                return Ok(orderItems);
            }
            else
            {
                return BadRequest("Error in Get Items Of Orders: There is no Items or Order");
            }
        }

        // GET: api/Orders/5
        [HttpGet("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> GetOrderByID(int id)
        {
            if (id < 0) return BadRequest("Error in Get Order by ID: missing ID");

            try
            {
                return Ok(await _orderORM.GetByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get Order by ID: " + ex.Message);
            }
        }

        // POST: api/Orders
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> CreateOrder([FromBody] Order order)
        {
            var idToken = Int32.Parse(HttpContext.User!
                         .Claims!
                         .FirstOrDefault(c => c.Type == "id")!
                         .Value);
            order.UserId = idToken;

            try
            {
                return Created("Successful Created!", await _orderORM.Create(order));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Create Order: " + ex.Message);
            }
        }

        // POST: api/Orders/AddItem
        [Route("AddItem")]
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> AddItemToOrder([FromBody] OrderItem orderItem)
        {
            try
            {
                return Created("Successful Adding!", await _orderORM.AddItem(orderItem));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Add Item To Order: " + ex.Message);
            }
        }

        // PUT: api/Orders
        [HttpPut("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin, Employee")]
        public async Task<ActionResult> UpdateOrderByID(int id, [FromBody] Order order)
        {
            if (id < 0) return BadRequest("Error in Update Order: missing ID");

            try
            {
                return Ok(await _orderORM.UpdateByID(id, order));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update Order: " + ex.Message);
            }
        }

        // DELETE: api/Orders/5
        [HttpDelete("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin, Employee")]
        public async Task<ActionResult> DeleteOrderByID(int id)
        {
            if (id < 0) return BadRequest("Error in Delete Order: missing ID");

            try
            {
                return Ok(await _orderORM.DeleteByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Order: " + ex.Message);
            }
        }
    }
}
