using System.ComponentModel.DataAnnotations;

namespace Backend.Models.DataModels
{
    public enum Role
    {
        Client,
        Employee,
        Admin
    }

    public class User : BaseEntity
    {
        [Required]
        [MinLength(2, ErrorMessage = "The Name value must be 2 numbers.")]
        public string Name { get; set; }
        [Required ]
        [EmailAddress(ErrorMessage = "The Email value must has an email format valid.")]
        public string Email { get; set; }
        [Required]
        [MinLength(8, ErrorMessage = "The Password value must be 8 characters minimum.")]
        public string Password { get; set; }
        [Required]
        public string Address { get; set; }
        [Required]
        [MinLength(8, ErrorMessage = "The Phone value must be 9 numbers.")]

        public string Phone { get; set; }
        public string Salt { get; set; }
        public Role Role { get; set; } = Role.Client;
    }
}
