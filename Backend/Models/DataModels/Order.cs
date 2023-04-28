using System.ComponentModel.DataAnnotations;

namespace Backend.Models.DataModels
{
    public class Order : BaseEntity
    {
        public int UserId { get; set; }
        [Required]
        public string Address { get; set; }
        public string State { get; set; }
    }
}
