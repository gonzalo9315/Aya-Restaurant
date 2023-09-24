using System.ComponentModel.DataAnnotations;

namespace Backend.Models.DataModels
{
    public class Category : BaseEntity
    {
        [Required]
        public string Name { get; set; }
        [Required]
        public string Image { get; set; }
    }
}
