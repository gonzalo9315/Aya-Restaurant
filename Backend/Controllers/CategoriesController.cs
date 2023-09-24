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
    public class CategoriesController: Controller
    {
        private readonly ICategoryORM _categoryORM;

        public CategoriesController(ICategoryORM categoryOrm)
        {
            _categoryORM = categoryOrm;
        }

        // GET: api/Categories
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> GetAllCategories()
        {
            var categories = await _categoryORM.GetAll();

            if (categories.LongCount() > 0)
            {
                return Ok(categories);
            }
            else
            {
                return BadRequest("Error in Get Categories: There is no category");
            }
        }

        // GET: api/Categories/Avalaible
        [Route("Avalaible")]
        [HttpGet]
        public async Task<ActionResult> GetAllCategoriesAvalaible()
        {
            var categories = await _categoryORM.GetAllAvalaible();

            if (categories.LongCount() > 0)
            {
                return Ok(categories);
            }
            else
            {
                return BadRequest("Error in Get Avalaible Categories: There is no category");
            }
        }

        // GET: api/Categories/5
        [HttpGet("{id}")]
        public async Task<ActionResult> GetCategoryByID(int id)
        {
            if (id < 0) return BadRequest("Error in Get Category by ID: missing ID");

            try
            {
                return Ok(await _categoryORM.GetByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get Category by ID: " + ex.Message);
            }
        }

        // POST: api/Categories
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> CreateCategory([FromBody] Category category)
        {
            try
            {
                return Created("Successful Created!", await _categoryORM.Create(category));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Create Category: " + ex.Message);
            }
        }

        // POST: api/Categories/AddDish
        [Route("AddDish")]
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> AddDishToCategory([FromBody] DishCategory dishCategory)
        {
            try
            {
                return Created("Successful Adding!", await _categoryORM.AddDish(dishCategory));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Add Dish To Category: " + ex.Message);
            }
        }

        // PUT: api/Categories/5
        [HttpPut("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> UpdateCategoryByID(int id, [FromBody] Category category)
        {
            if (id < 0) return BadRequest("Error in Update Category: missing ID");

            try
            {
                return Ok(await _categoryORM.UpdateByID(id, category));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update Category: " + ex.Message);
            }
        }

        // DELETE: api/Categories/5
        [HttpDelete("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteCategoryByID(int id)
        {
            if (id < 0) return BadRequest("Error in Delete Category: missing ID");

            try
            {
                return Ok(await _categoryORM.DeleteByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Category: " + ex.Message);
            }
        }

        // DELETE: api/Categories/DeleteDish
        [Route("DeleteDish")]
        [HttpDelete]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteDishToCategory([FromBody] DishCategory dishCategory)
        {
            try
            {
                return Ok(await _categoryORM.DeleteDish(dishCategory));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Dish To Category: " + ex.Message);
            }
        }
    }
}
