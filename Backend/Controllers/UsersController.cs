using Backend.Helpers;
using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using System.Linq;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : Controller
    {
        private readonly IUserORM _userORM;

        public UsersController(IUserORM userORM)
        {
            _userORM= userORM;
        }

        // GET: api/Users
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> GetAllUsers()
        {
            var users = await _userORM.GetAll();

            if(users.LongCount() > 0)
            {
                return Ok(users);
            }
            else
            {
                return BadRequest("Error in Get Users: There is no users");
            }
        }

        // GET: api/Users/5
        [HttpGet("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<IActionResult> GetUserByID(int id)
        {
            if (id < 0) return BadRequest("Error in Get User by ID: missing ID");

            try
            {
                return Ok(await _userORM.GetByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get User by ID: " + ex.Message);
            }
        }

        // POST: api/Users
        [HttpPost]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> CreateUser([FromBody] User user)
        {
            try
            {
                user = HashHelper.HashedUsuario(user);
                return Created("Successful Created!", await _userORM.Create(user));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Create User: " + ex.Message);
            }

        }

        // PUT: api/Users/5
        [HttpPut("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> UpdateUserByID(int id, [FromBody] User user)
        {
            if (id < 0) return BadRequest("Error in Update User: missing ID");

            try
            {
                user = HashHelper.HashedUsuario(user);
                return Ok(await _userORM.UpdateByID(id, user));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update User: " + ex.Message);
            }
        }

        // DELETE: api/Users/5
        [HttpDelete("{id}")]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin")]
        public async Task<ActionResult> DeleteUserByID(int id)
        {
            if(id < 0) return BadRequest("Error in Delete User: missing ID");

            try
            {
                return Ok(await _userORM.DeleteByID(id));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete User: " + ex.Message);
            }
        }
    }
}
