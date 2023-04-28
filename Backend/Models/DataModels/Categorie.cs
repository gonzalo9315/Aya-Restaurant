using System.ComponentModel.DataAnnotations;

namespace Backend.Models.DataModels
{
    public class Categorie : BaseEntity
    {
        [Required]

        public string Name { get; set; }
    }
}
