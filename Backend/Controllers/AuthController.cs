using Backend.Helpers;
using Backend.Domain.Interfaces;
using Backend.Models.DataModels;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System;
using System.Linq;
using Backend.Models;

namespace Backend.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IAuthORM _authORM;

        public AuthController(IAuthORM authORM)
        {
            _authORM = authORM;
        }

        // POST: api/Auth/Login
        [HttpPost]
        public ActionResult Login([FromBody] UserLogin user)
        {
            try
            {
                return Ok( _authORM.Login(user.Email, user.Password));
            }
            catch(Exception ex)
            {
                return BadRequest("Login Error: " + ex.Message);
            }
        }

        // POST: api/Auth/Register
        [HttpPost]
        public async Task<ActionResult> Register([FromBody] User user)
        {
            try
            {
                user = HashHelper.HashedUsuario(user);
                return Created("Successful registration!", await _authORM.Register(user));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Register: " + ex.Message);
            }
        }

        // PUT: api/Auth/UpdateMyData
        [HttpPut]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> UpdateMyData([FromBody] User user)
        {
            var idToken = Int32.Parse(HttpContext.User!
                                                 .Claims!
                                                 .FirstOrDefault(c => c.Type == "id")!
                                                 .Value);

            try
            {
                user = HashHelper.HashedUsuario(user);
                return Ok(await _authORM.Update(idToken, user));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Update my Account: " + ex.Message);
            }
        }

        // GET: api/Auth/GetMyProfile
        [HttpGet]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        public async Task<ActionResult> GetMyProfile()
        {
            var idToken = Int32.Parse(HttpContext.User!
                                                 .Claims!
                                                 .FirstOrDefault(c => c.Type == "id")!
                                                 .Value);

            try
            {
                return Ok(await _authORM.Profile(idToken));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Get Data Profile: " + ex.Message);
            }
        }

        // GET: api/Auth/DeleteMyAccount
        [HttpDelete]
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme, Roles = "Admin, Client")]
        public async Task<ActionResult> DeleteMyAccount()
        {
            var idToken = Int32.Parse(HttpContext.User!
                                                 .Claims!
                                                 .FirstOrDefault(c => c.Type == "id")!
                                                 .Value);

            try
            {
                return Ok(await _authORM.Delete(idToken));
            }
            catch (Exception ex)
            {
                return BadRequest("Error in Delete Account: " + ex.Message);
            }
        }
    }
}
