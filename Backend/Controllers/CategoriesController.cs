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
        private readonly ICategorieORM _categorieORM;

        public CategoriesController(ICategorieORM categorieOrm)
        {
            _categorieORM = categorieOrm;
        }

        // GET: api/Categories
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> GetAllCategories()
        {
            var categories = await _categorieORM.GetAll();

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
            var categories = await _categorieORM.GetAllAvalaible();

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
        public async Task<ActionResult> GetCategorieByID(int id)
        {
            if (id < 0) return BadRequest("Error in Get Categorie by ID: missing ID");

            try
            {
                return Ok(await _categorieORM.GetByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get Categorie by ID: " + ex.Message);
            }
        }

        // POST: api/Categories
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> CreateCategorie([FromBody] Categorie categorie)
        {
            try
            {
                return Created("Successful Created!", await _categorieORM.Create(categorie));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Create Categorie: " + ex.Message);
            }
        }

        // POST: api/Categories/AddDish
        [Route("AddDish")]
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> AddDishToCategorie([FromBody] DishCategorie dishCategorie)
        {
            try
            {
                return Created("Successful Adding!", await _categorieORM.AddDish(dishCategorie));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Add Dish To Categorie: " + ex.Message);
            }
        }

        // PUT: api/Categories/5
        [HttpPut("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> UpdateCategorieByID(int id, [FromBody] Categorie categorie)
        {
            if (id < 0) return BadRequest("Error in Update Categorie: missing ID");

            try
            {
                return Ok(await _categorieORM.UpdateByID(id, categorie));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update Categorie: " + ex.Message);
            }
        }

        // DELETE: api/Categories/5
        [HttpDelete("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteCategorieByID(int id)
        {
            if (id < 0) return BadRequest("Error in Delete Categorie: missing ID");

            try
            {
                return Ok(await _categorieORM.DeleteByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Categorie: " + ex.Message);
            }
        }

        // DELETE: api/Categories/DeleteDish
        [Route("DeleteDish")]
        [HttpDelete]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteDishToCategorie([FromBody] DishCategorie dishCategorie)
        {
            try
            {
                return Ok(await _categorieORM.DeleteDish(dishCategorie));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Dish To Categorie: " + ex.Message);
            }
        }
    }
}
