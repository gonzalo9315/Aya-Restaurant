using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using Backend.Models;
using Backend.Models.DataModels;
using System;
using System.Collections.Generic;

namespace Backend.Helpers
{
    public static class JwtHelper
    {
        public static IEnumerable<Claim> GetClaims(this UserTokens userAccounts, Guid Id)
        {
            List<Claim> claims = new List<Claim>
            {
                new Claim("id", userAccounts.Id.ToString()),
                new Claim(ClaimTypes.Name, userAccounts.Username),
                new Claim(ClaimTypes.NameIdentifier, Id.ToString()),
                new Claim(ClaimTypes.Expiration, DateTime.UtcNow.AddDays(1).ToString("MMM ddd dd yyyy HH:mm:ss tt"))
            };

            if(userAccounts.Role == Role.Admin)
            {
                claims.Add(new Claim(ClaimTypes.Role, "Admin"));
            }
            else if (userAccounts.Role == Role.Employee)
            {
                claims.Add(new Claim(ClaimTypes.Role, "Employee"));
            }
            else
            {
                claims.Add(new Claim(ClaimTypes.Role, "Client"));
            }

            return claims;
        }

        public static IEnumerable<Claim> GetClaims(this UserTokens userAccounts, out Guid Id)
        {
            Id = Guid.NewGuid();
            return GetClaims(userAccounts, Id);
        }

        public static UserTokens GetTokenKey(UserTokens model, JwtSettings jwtSettings)
        {
            try
            {
                var userToken = new UserTokens();
                if(model == null)
                {
                    throw new ArgumentNullException(nameof(model));
                }
                // Obtain SECRET KEY
                var key = System.Text.Encoding.ASCII.GetBytes(jwtSettings.IssuerSigningKey);

                Guid Id;

                // Expire in 1 Day
                DateTime expireTime = DateTime.UtcNow.AddDays(1);

                // Validity of our token
                userToken.Validity = expireTime.TimeOfDay;

                // GENERATE OUR JWT
                var jwToken = new JwtSecurityToken(

                    issuer: jwtSettings.ValidIssuer,
                    audience: jwtSettings.ValidAudience,
                    claims: GetClaims(model, out Id),
                    notBefore: new DateTimeOffset(DateTime.Now).DateTime,
                    expires: new DateTimeOffset(expireTime).DateTime,
                    signingCredentials: new SigningCredentials(
                            new SymmetricSecurityKey(key),
                            SecurityAlgorithms.HmacSha256
                    )
                );
                userToken.Token = new JwtSecurityTokenHandler().WriteToken(jwToken);
                userToken.Username = model.Username;
                userToken.Id = model.Id;
                userToken.GuidId = Id;
                userToken.Role = model.Role;
                return userToken;

            }catch(Exception exception)
            {
                throw new Exception("Error Generatin the JWT", exception);
            }
        }
    }
}
