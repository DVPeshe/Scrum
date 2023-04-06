package ru.agile.scrum.mst.market.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.agile.scrum.mst.market.auth.entities.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
