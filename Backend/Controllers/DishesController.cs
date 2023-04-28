using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using System.Linq;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DishesController : Controller
    {
        private readonly IDishORM _dishORM;

        public DishesController(IDishORM dishORM)
        {
            _dishORM = dishORM;
        }

        // GET: api/Dishes
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> GetAllDishes()
        {
            var dishes = await _dishORM.GetAll();

            if (dishes.LongCount() > 0)
            {
                return Ok(dishes);
            }
            else
            {
                return BadRequest("Error in Get Dishes: There is no dishes");
            }
        }

        // GET: api/Dishes/Categorie/5
        [Route("categorie/{id}")]
        [HttpGet]
        public async Task<ActionResult> GetAllDishesByCategorie(int id)
        {
            var dishes = await _dishORM.GetAllByCategorie(id);

            if (dishes.LongCount() > 0)
            {
                return Ok(dishes);
            }
            else
            {
                return BadRequest("Error in Get Dishes By Categorie: There is no dishes");
            }
        }

        // GET: api/Dishes/5
        [HttpGet("{id}")]
        public async Task<ActionResult> GetDishByID(int id)
        {
            if (id < 0) return BadRequest("Error in Get Dish by ID: missing ID");

            try
            {
                return Ok(await _dishORM.GetByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get Dish by ID: " + ex.Message);
            }
        }

        // POST: api/Dishes
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> CreateDish([FromBody] Dish dish)
        {
            try
            {
                return Created("Successful Created!", await _dishORM.Create(dish));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Create Dish: " + ex.Message);
            }
        }

        // PUT: api/Dishes
        [HttpPut("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> UpdateDishByID(int id, [FromBody] Dish dish)
        {
            if (id < 0) return BadRequest("Error in Update Dish by ID: missing ID");

            try
            {
                return Ok(await _dishORM.UpdateByID(id, dish));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update Dish: " + ex.Message);
            }
        }

        // DELETE: api/Dishes
        [HttpDelete("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteDishByID(int id)
        {
            if (id < 0) return BadRequest("Error in Delete Dish: missing ID");

            try
            {
                return Ok(await _dishORM.DeleteByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Dish: " + ex.Message);
            }
        }
    }
}
