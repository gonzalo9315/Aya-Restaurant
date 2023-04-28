using Backend.Models.DataModels;
using System;

namespace Backend.Models
{
    public class UserTokens
    {
        public int Id { get; set; }
        public string Token { get; set; } = string.Empty;
        public string Username { get; set; } = string.Empty;
        public TimeSpan Validity { get; set; }
        public string ResfreshToken { get; set; } = string.Empty;
        public Guid GuidId { get; set; }
        public DateTime ExpiredTime { get; set; }
        public Role Role { get; set; }
    }
}
